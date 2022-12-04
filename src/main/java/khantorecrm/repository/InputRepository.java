package khantorecrm.repository;

import khantorecrm.model.Input;
import khantorecrm.model.enums.ActionType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InputRepository extends JpaRepository<Input, Long> {
    List<Input> findAllByStatus(ActionType wait);

    List<Input> findAllByType(ProductType type);

    List<Input> findAllByStatusAndCreatedBy_Role_RoleName(ActionType status, RoleName createdBy_role_roleName);

    List<Input> findAllByStatusAndCreatedBy_Id(ActionType wait, Long id);
}
