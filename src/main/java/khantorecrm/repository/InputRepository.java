package khantorecrm.repository;

import khantorecrm.model.Input;
import khantorecrm.model.enums.ActionType;
import khantorecrm.model.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InputRepository extends JpaRepository<Input, Long> {
    List<Input> findAllByStatus(ActionType wait);

    List<Input> findAllByType(ProductType type);
}
