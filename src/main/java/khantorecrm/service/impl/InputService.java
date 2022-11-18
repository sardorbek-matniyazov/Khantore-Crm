package khantorecrm.service.impl;

import khantorecrm.model.Employee;
import khantorecrm.model.Input;
import khantorecrm.model.Product;
import khantorecrm.model.ProductItem;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.InputDto;
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

import java.util.List;

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
                            product.getType(),
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
}
