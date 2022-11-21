package khantorecrm.repository;

import khantorecrm.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByPhoneNumber(String name);

    boolean existsByPhoneNumberAndIdIsNot(String phoneNumber, Long id);
}
