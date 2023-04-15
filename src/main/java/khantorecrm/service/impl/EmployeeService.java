package khantorecrm.service.impl;

import khantorecrm.model.Balance;
import khantorecrm.model.Employee;
import khantorecrm.model.User;
import khantorecrm.model.enums.RoleName;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.EmployeeDto;
import khantorecrm.repository.EmployeeRepository;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.Deletable;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.service.functionality.Updatable;
import khantorecrm.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static khantorecrm.utils.constants.Statics.getCurrentUser;
import static khantorecrm.utils.constants.Statics.isNonDeletable;

@Service
public class EmployeeService implements
        InstanceReturnable<Employee, Long>,
        Creatable<EmployeeDto>,
        Deletable<Long>,
        Updatable<EmployeeDto, Long> {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employee> getAllInstances() {
        return repository.findAll();
    }

    @Override
    public Employee getInstanceWithId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public OwnResponse create(EmployeeDto dto) {
        if (repository.existsByPhoneNumber(dto.getPhoneNumber())) {
            return OwnResponse.EMPLOYEE_ALREADY_EXISTS.setMessage("Employee with phone number " + dto.getPhoneNumber() + " already exists");
        }

        return OwnResponse.CREATED_SUCCESSFULLY.setData(
                repository.save(
                        new Employee(
                                dto.getName(),
                                dto.getPhoneNumber(),
                                dto.getComment(),
                                new Balance()
                        )
                )
        );
    }

    @Override
    public OwnResponse update(EmployeeDto dto, Long id) {
        if (repository.existsByPhoneNumberAndIdIsNot(dto.getPhoneNumber(), id)) {
            return OwnResponse.EMPLOYEE_ALREADY_EXISTS.setMessage("Employee with phone number " + dto.getPhoneNumber() + " already exists");
        }

        repository.findById(id).ifPresent(
                employee -> {
                    employee.setName(dto.getName());
                    employee.setPhoneNumber(dto.getPhoneNumber());
                    employee.setComment(dto.getComment());
                    repository.save(employee);
                }
        );

        return OwnResponse.UPDATED_SUCCESSFULLY;
    }

    @Override
    public OwnResponse delete(Long id) {
        try {
            final User currentUser = getCurrentUser();

            final Employee employee = repository.findById(id).orElseThrow(
                    () -> new NotFoundException("Employee with id " + id + " not found")
            );

            if (!currentUser.getRole().getRoleName().equals(RoleName.ADMIN) && (isNonDeletable(employee.getCreatedAt().getTime()) && !employee.getCreatedBy().getId().equals(currentUser.getId()))) {
                return OwnResponse.CANT_DELETE;
            }
            repository.deleteById(id);
            return OwnResponse.DELETED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.CANT_DELETE.setMessage("Can't delete employee");
        }
    }
}
