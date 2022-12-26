package khantorecrm.repository;

import khantorecrm.model.DeliveryMovingProductHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryMovingProductHistoryRepository extends JpaRepository<DeliveryMovingProductHistory, Long> {
    List<DeliveryMovingProductHistory> findAllByToDeliveryId(Long id, Sort sort);
}
