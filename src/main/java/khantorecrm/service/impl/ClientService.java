package khantorecrm.service.impl;

import khantorecrm.model.Balance;
import khantorecrm.model.Client;
import khantorecrm.model.User;
import khantorecrm.model.enums.ClientType;
import khantorecrm.model.enums.RoleName;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ClientDto;
import khantorecrm.repository.ClientRepository;
import khantorecrm.service.IClientService;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.InstanceReturnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static khantorecrm.utils.constants.Statics.getCurrentUser;

@Service
public class ClientService implements
        InstanceReturnable<Client, Long>,
        Creatable<ClientDto>,
        IClientService {
    private final ClientRepository repository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.repository = clientRepository;
    }

    @Override
    public List<Client> getAllInstances() {
        final User user = getCurrentUser();
        if (Objects.requireNonNull(user.getRole().getRoleName()) == RoleName.ADMIN) {
            return repository.findAll();
        }
        return repository.findAllByCreatedBy_Id(user.getId());
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
    public OwnResponse paymentToBalance(ClientDto dto) {
        return null;
    }
}
