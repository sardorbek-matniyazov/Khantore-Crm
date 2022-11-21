package khantorecrm.service.impl;

import khantorecrm.model.Employee;
import khantorecrm.model.Ingredient;
import khantorecrm.model.Input;
import khantorecrm.model.Product;
import khantorecrm.model.ProductItem;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.InputDto;
import khantorecrm.payload.dto.ProductItemList;
import khantorecrm.repository.BalanceRepository;
import khantorecrm.repository.EmployeeRepository;
import khantorecrm.repository.InputRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.repository.ProductRepository;
import khantorecrm.service.IInputService;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.utils.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class InputService implements
        InstanceReturnable<Input, Long>,
        IInputService {

    private final InputRepository repository;
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public InputService(InputRepository repository, ProductItemRepository productItemRepository, ProductRepository productRepository, EmployeeRepository employeeRepository, BalanceRepository balanceRepository) {
        this.repository = repository;
        this.productItemRepository = productItemRepository;
        this.productRepository = productRepository;
        this.employeeRepository = employeeRepository;
        this.balanceRepository = balanceRepository;
    }

    @Override
    public List<Input> getAllInstances() {
        return repository.findAll();
    }

    @Override
    public Input getInstanceWithId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public OwnResponse incomeIngredient(InputDto dto) {
        try {
            ProductItem productItem = productItemRepository.findById(dto.getProductItemId()).orElseThrow(
                    () -> new ProductNotFoundException("Product item with id " + dto.getProductItemId() + " not found")
            );

            Product product = productItem.getItemProduct();

            Employee employee = employeeRepository.findById(dto.getEmployerId()).orElseThrow(
                    () -> new ProductNotFoundException("Employee with id " + dto.getEmployerId() + " not found")
            );

            // save product item
            productItem.setItemAmount(productItem.getItemAmount() + dto.getAmount());
            product.setPrice(dto.getPrice());

            // save employee
            employee.getBalance().setAmount(employee.getBalance().getAmount() - dto.getPrice() * dto.getAmount());

            repository.save(
                    new Input(
                            productItemRepository.save(productItem),
                            dto.getAmount(),
                            ProductType.INGREDIENT,
                            dto.getPrice(),
                            employeeRepository.save(employee)
                    )
            );
        } catch (ProductNotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }

        return OwnResponse.CREATED_SUCCESSFULLY;
    }

    @Override
    public OwnResponse incomeProduct(ProductItemList dto) {
        try {
            ProductItem productItem = productItemRepository.findById(dto.getProductItemId()).orElseThrow(
                    () -> new ProductNotFoundException("Product item with id " + dto.getProductItemId() + " not found")
            );

            Input save = repository.save(
                    new Input(
                            productItem,
                            dto.getAmount(),
                            ProductType.PRODUCT,
                            productItem.getItemProduct().getPrice()
                    )
            );

            return OwnResponse.CREATED_SUCCESSFULLY.setData(save);
        } catch (ProductNotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    @Transactional
    public OwnResponse production(List<ProductItemList> dto) {
        try {
            dto.stream().map(
                    item -> {
                        ProductItem productItem = productItemRepository.findById(item.getProductItemId()).orElseThrow(
                                () -> new ProductNotFoundException("Product item with id " + item.getProductItemId() + " not found")
                        );

                        productItem.setItemAmount(productItem.getItemAmount() + item.getAmount());

                        return productItemRepository.save(productItem);
                    }
            ).forEach(
                    productItem -> {
                        boolean b = changeIngredients(productItem.getItemProduct().getIngredients(), productItem.getItemAmount(), '+');
                        if (b) throw new ProductNotFoundException("There are something wrong with ingredients");
                        Input save = repository.save(
                                new Input(
                                        productItem,
                                        productItem.getItemAmount(),
                                        ProductType.PRODUCT,
                                        productItem.getItemProduct().getPrice()
                                )
                        );
                    }
            );

            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (ProductNotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
    }

    private boolean changeIngredients(Set<Ingredient> ingredients, Double itemAmount, char ch) {
        try {
            Stream<ProductItem> productItemStream = ingredients.stream().map(
                    ingredient -> {
                        ProductItem productItem = productItemRepository.findById(ingredient.getProductItem().getId()).orElseThrow(
                                () -> new ProductNotFoundException("Product item with id " + ingredient.getProductItem().getId() + " not found")
                        );

                        if (ch == '+') {
                            productItem.setItemAmount(productItem.getItemAmount() - ingredient.getItemAmount() * itemAmount);
                        } else {
                            productItem.setItemAmount(productItem.getItemAmount() + ingredient.getItemAmount() * itemAmount);
                        }

                        return productItemRepository.save(productItem);
                    }
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
