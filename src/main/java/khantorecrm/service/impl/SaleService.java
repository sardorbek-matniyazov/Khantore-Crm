package khantorecrm.service.impl;

import khantorecrm.model.Client;
import khantorecrm.model.Output;
import khantorecrm.model.ProductItem;
import khantorecrm.model.Sale;
import khantorecrm.model.enums.OutputType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.SaleDto;
import khantorecrm.repository.ClientRepository;
import khantorecrm.repository.PaymentRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.repository.SaleRepository;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.utils.exceptions.NotFoundException;
import khantorecrm.utils.exceptions.TypesInError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SaleService implements InstanceReturnable<Sale, Long>, Creatable<SaleDto> {
    private final SaleRepository repository;
    private final ProductItemRepository itemRepository;
    private final ClientRepository clientRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public SaleService(SaleRepository repository, ProductItemRepository itemRepository, ClientRepository clientRepository, PaymentRepository paymentRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.clientRepository = clientRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Sale> getAllInstances() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Sale getInstanceWithId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public OwnResponse create(SaleDto dto) {
        try {
            ProductItem productItem = itemRepository.findById(dto.getClientId()).orElseThrow(
                    () -> new NotFoundException("Product item with id " + dto.getProductItemId() + " not found")
            );

            Client client = clientRepository.findById(dto.getClientId()).orElseThrow(
                    () -> new NotFoundException("Client with id " + dto.getClientId() + " not found")
            );

            if (productItem.getItemAmount() < dto.getAmount())
                throw new TypesInError("There arent enough product in the warehouse !");

            productItem.setItemAmount(productItem.getItemAmount() - dto.getAmount());

            Double wholePrice = dto.getAmount() * productItem.getItemProduct().getPrice();

            if (wholePrice < dto.getPaymentAmount())
                throw new TypesInError(wholePrice + " sale price is less than " + dto.getPaymentAmount());

            Sale save = repository.save(
                    new Sale(
                            new Output(
                                    productItem,
                                    dto.getAmount(),
                                    OutputType.SALE
                            ),
                            client,
                            wholePrice,
                            wholePrice - dto.getPaymentAmount(),
                            dto.getPaymentAmount(),
                            productItem.getItemProduct().getPrice()
                    )
            );

            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
    }
}
