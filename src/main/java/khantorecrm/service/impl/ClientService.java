package khantorecrm.service.impl;

import khantorecrm.model.Balance;
import khantorecrm.model.Client;
import khantorecrm.model.Payment;
import khantorecrm.model.Sale;
import khantorecrm.model.User;
import khantorecrm.model.enums.ClientType;
import khantorecrm.model.enums.PaymentStatus;
import khantorecrm.model.enums.RoleName;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ClientDto;
import khantorecrm.payload.dto.PaymentDto;
import khantorecrm.repository.BalanceRepository;
import khantorecrm.repository.ClientRepository;
import khantorecrm.repository.SaleRepository;
import khantorecrm.service.IClientService;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.Deletable;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.service.functionality.Updatable;
import khantorecrm.utils.exceptions.NotFoundException;
import khantorecrm.utils.exceptions.TypesInError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static khantorecrm.utils.constants.Statics.getCurrentUser;

@Service
public class ClientService implements
        InstanceReturnable<Client, Long>,
        Creatable<ClientDto>,
        IClientService,
        Updatable<ClientDto, Long>,
        Deletable<Long> {
    private final ClientRepository repository;
    private final BalanceRepository balanceRepository;
    private final SaleRepository saleRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, BalanceRepository balanceRepository, SaleRepository saleRepository) {
        this.repository = clientRepository;
        this.balanceRepository = balanceRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Client> getAllInstances() {
        final User user = getCurrentUser();
        if (Objects.requireNonNull(user.getRole().getRoleName()) == RoleName.ADMIN) {
            return repository.findAll();
        }
        return repository.findAllByCreatedBy_IdOrCreatedBy_Id(user.getId(), 1L, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Client getInstanceWithId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public OwnResponse create(ClientDto dto) {
        if (repository.existsByNameAndPhone(dto.getName(), dto.getPhone())) return OwnResponse.CLIENT_ALREADY_EXISTS;
        repository.save(
                new Client(
                        dto.getName(),
                        dto.getPhone(),
                        dto.getComment(),
                        ClientType.BASIC,
                        new Balance()
                )
        );
        return OwnResponse.CREATED_SUCCESSFULLY;
    }

    public List<Client> debtClients() {
        final User user = getCurrentUser();
        final List<Client> allByBalanceAmountSmallerThan0 = repository.findAllByBalance_AmountSmallerThan0();
        if (user.getRole().getRoleName().equals(RoleName.ADMIN))
            return allByBalanceAmountSmallerThan0;
        return allByBalanceAmountSmallerThan0
                .stream()
                .filter(client -> client.getCreatedBy().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public OwnResponse paymentToBalance(Long id, PaymentDto dto) {
        try {
            final Client client = repository.findById(id).orElseThrow(
                    () -> new NotFoundException("Client not found")
            );

            final Balance balance = client.getBalance();
            balance.setAmount(balance.getAmount() + dto.getAmount());
            balance.getPayments().add(
                    new Payment(
                            dto.getAmount(),
                            dto.getType(),
                            PaymentStatus.INCOME
                    )
            );

            // increasing sale amounts by client
            increasingSaleAmountsByClient(id, new AtomicReference<>(dto.getAmount()));

            // saving balance
            balanceRepository.save(balance);
            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR;
        }
    }

    private void increasingSaleAmountsByClient(Long clientId, final AtomicReference<Double> amount) {
        final List<Sale> sales = saleRepository.findAllByClient_Id(clientId, Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(sale -> {
                    if (amount.get() > 0) {
                        if (sale.getDebtPrice() > amount.get()) {
                            sale.setDebtPrice(sale.getDebtPrice() - amount.get());
                            amount.set(0.0);
                        } else {
                            amount.set(amount.get() - sale.getDebtPrice());
                            sale.setDebtPrice(0.0);
                        }
                        return sale;
                    } return null;
                }).collect(Collectors.toList());
        if (amount.get() > 0) {
            throw new TypesInError("Amount is greater than debt");
        } else {
            // saving sales
            saleRepository.saveAll(sales);
        }
    }

    @Override
    public OwnResponse update(ClientDto dto, Long id) {
        try {
            if (repository.existsByPhoneAndIdIsNot(dto.getPhone(), id)) return OwnResponse.CLIENT_ALREADY_EXISTS;
            final Client client = repository.findById(id).orElseThrow(
                    () -> new NotFoundException("Client not found")
            );
            client.setName(dto.getName());
            client.setPhone(dto.getPhone());
            client.setComment(dto.getComment());
            repository.save(client);
            return OwnResponse.UPDATED_SUCCESSFULLY;
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR;
        }
    }

    @Override
    public OwnResponse delete(Long id) {
        try {
            final Client client = repository.findById(id).orElseThrow(
                    () -> new NotFoundException("Client not found")
            );
            repository.delete(client);
            return OwnResponse.DELETED_SUCCESSFULLY.setMessage("Client deleted successfully");
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.CANT_DELETE.setMessage("Client can't be deleted");
        }
    }
}
