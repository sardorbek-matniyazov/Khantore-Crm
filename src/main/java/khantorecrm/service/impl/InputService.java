package khantorecrm.service.impl;

import khantorecrm.model.*;
import khantorecrm.model.enums.ActionType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.InputDto;
import khantorecrm.payload.dto.ProductItemWrapper;
import khantorecrm.repository.*;
import khantorecrm.service.IInputService;
import khantorecrm.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class InputService implements
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
    public OwnResponse incomeIngredient(InputDto dto) {
        try {
            ProductItem productItem = productItemRepository.findById(dto.getProductItemId()).orElseThrow(
                    () -> new NotFoundException("Product item with id " + dto.getProductItemId() + " not found")
            );

            Product product = productItem.getItemProduct();

            Employee employee = employeeRepository.findById(dto.getEmployerId()).orElseThrow(
                    () -> new NotFoundException("Employee with id " + dto.getEmployerId() + " not found")
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
                            employeeRepository.save(employee),
                            ActionType.ACCEPTED
                    )
            );
        } catch (NotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (NullPointerException e) {
            return OwnResponse.ERROR.setMessage("There is no such product item");
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }

        return OwnResponse.CREATED_SUCCESSFULLY;
    }

    @Override
    @Transactional
    public OwnResponse production(ProductItemWrapper dto) {
        try {
            dto.getItems().stream().map(
                    item -> {
                        ProductItem productItem = productItemRepository.findById(item.getProductItemId()).orElseThrow(
                                () -> new NotFoundException("Product item with id " + item.getProductItemId() + " not found")
                        );

                        productItem.setItemAmount(productItem.getItemAmount() + item.getAmount());

                        return productItemRepository.save(productItem);
                    }
            ).forEach(
                    productItem -> {
                        boolean b = changeIngredients(productItem.getItemProduct().getIngredients(), productItem.getItemAmount(), '+');
                        if (!b) throw new NotFoundException("There are something wrong with ingredients");
                        Input save = repository.save(
                                new Input(
                                        productItem,
                                        productItem.getItemAmount(),
                                        ProductType.PRODUCT,
                                        productItem.getItemProduct().getPrice(),
                                        ActionType.ACCEPTED
                                )
                        );
                    }
            );

            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    public List<Input> getAllInputsByStatus(ActionType wait) {
        return repository.findAllByStatus(wait);
    }

    private boolean changeIngredients(Set<ItemForCollection> ingredients, Double itemAmount, char ch) {
        try {
            Stream<ProductItem> productItemStream = ingredients.stream().map(
                    ingredient -> {
                        ProductItem productItem = productItemRepository.findById(ingredient.getProductItem().getId()).orElseThrow(
                                () -> new NotFoundException("Product item with id " + ingredient.getProductItem().getId() + " not found")
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

    @Override
    public List<Input> getAllByType(ProductType type) {
        return repository.findAllByType(type);
    }
}
