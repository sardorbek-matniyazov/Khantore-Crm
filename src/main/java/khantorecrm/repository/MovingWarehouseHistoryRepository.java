package khantorecrm.repository;

import khantorecrm.model.WarehouseMovingProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovingWarehouseHistoryRepository extends JpaRepository<WarehouseMovingProductHistory, Long> {
}
