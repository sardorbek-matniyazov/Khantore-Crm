package khantorecrm.repository;

import khantorecrm.model.Output;
import khantorecrm.model.enums.OutputType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutputRepository extends JpaRepository<Output, Long> {
    List<Output> findAllByType(OutputType type);

    @Query(value = "select o from output o where o.delivery.id = ?1")
    List<Output> findAllByDelivery_Id(Long id);
}
