package khantorecrm.service.impl;

import khantorecrm.model.Employee;
import khantorecrm.model.Input;
import khantorecrm.model.ItemForCollection;
import khantorecrm.model.Product;
import khantorecrm.model.ProductItem;
import khantorecrm.model.enums.ActionType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.model.enums.RoleName;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.InputDto;
import khantorecrm.payload.dto.ProductItemListDto;
import khantorecrm.payload.dto.ProductItemWrapper;
import khantorecrm.repository.EmployeeRepository;
import khantorecrm.repository.InputRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.service.IInputService;
import khantorecrm.utils.exceptions.NotFoundException;
import khantorecrm.utils.exceptions.TypesInError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InputService implements
        IInputService {

    private final InputRepository repository;
    private final ProductItemRepository productItemRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public InputService(
            InputRepository repository,
            ProductItemRepository productItemRepository,
            EmployeeRepository employeeRepository
    ) {
        this.repository = repository;
        this.productItemRepository = productItemRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public OwnResponse incomeIngredient(InputDto dto) {
        try {
            Employee employee = employeeRepository.findById(dto.getEmployerId()).orElseThrow(
                    () -> new NotFoundException("Employee with id " + dto.getEmployerId() + " not found")
            );

            creatingInput(dto.getItems(), employee, null);

            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    @Transactional
    public OwnResponse production(ProductItemWrapper dto) {
        try {
            creatingInput(dto.getItems(), null, dto.getDate());
            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    public List<Input> getAllInputsByStatus(ActionType wait) {
        return repository.findAllByStatusAndCreatedBy_Role_RoleName(wait, RoleName.DRIVER);
    }

    @Override
    public List<Input> getAllByType(ProductType type) {
        return repository.findAllByType(type);
    }

    private Set<ProductItem> changeIngredients(Set<ItemForCollection> ingredients, Double itemAmount, char ch) {
        return ingredients.stream().map(
                ingredient -> {
                    final ProductItem productItem = ingredient.getProductItem();

                    if (ch == '+') {
                        productItem.setItemAmount(productItem.getItemAmount() - ingredient.getItemAmount() * itemAmount);
                    } else {
                        productItem.setItemAmount(productItem.getItemAmount() + ingredient.getItemAmount() * itemAmount);
                    }

                    return productItemRepository.save(productItem);
                }
        ).collect(Collectors.toSet());
    }

    private void creatingInput(List<ProductItemListDto> items, Employee employee, String date) {
        items.forEach(
                item -> {
                    ProductItem productItem = productItemRepository.findById(item.getProductItemId()).orElseThrow(
                            () -> new NotFoundException("Product item with id " + item.getProductItemId() + " not found")
                    );

                    if (productItem.getItemProduct().getType().equals(ProductType.INGREDIENT))
                        throw new TypesInError("Product type should be PRODUCT");

                    productItem.setItemAmount(productItem.getItemAmount() + item.getAmount());

                    final ProductItem save = productItemRepository.save(productItem);

                    // decreasing ingredients
                    if (employee == null) {
                        final Set<ProductItem> productItems =
                                changeIngredients(productItem.getItemProduct().getIngredients(), item.getAmount(), '+');

                        if (productItems.size() == 0)
                            throw new NotFoundException("There are something wrong with ingredients, format");
                    }

                    // saving input
                    if (date != null) {
                        repository.save(
                                new Input(
                                        productItem,
                                        item.getAmount(),
                                        ProductType.PRODUCT,
                                        productItem.getItemProduct().getPrice(),
                                        null,
                                        ActionType.ACCEPTED
                                ).setCreatedDate(date)
                        );
                    } else if (employee != null) {
                        employee.getBalance().setAmount(
                                employee.getBalance().getAmount() - productItem.getItemProduct().getPrice() * item.getAmount()
                        );
                        repository.save(
                                new Input(
                                        productItem,
                                        item.getAmount(),
                                        ProductType.PRODUCT,
                                        productItem.getItemProduct().getPrice(),
                                        employee,
                                        ActionType.ACCEPTED
                                )
                        );
                    } else {
                        throw new TypesInError("Something with inputs");
                    }
                }
        );
    }
}
