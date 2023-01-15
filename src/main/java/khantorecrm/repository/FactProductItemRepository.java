package khantorecrm.repository;

import khantorecrm.model.FactProductItemDaily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FactProductItemRepository extends JpaRepository<FactProductItemDaily, Long> {
    Optional<FactProductItemDaily> findByIdAndDate(Long productItemId, String date);
}
