package khantorecrm.repository;

import khantorecrm.model.Output;
import khantorecrm.model.enums.OutputType;
import org.hibernate.criterion.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutputRepository extends JpaRepository<Output, Long> {
    List<Output> findAllByType(OutputType type);

    List<Output> findAllByCreatedBy_Id(Long id);
}
