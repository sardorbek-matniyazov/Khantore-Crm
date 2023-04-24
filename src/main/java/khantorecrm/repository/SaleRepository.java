package khantorecrm.repository;

import khantorecrm.model.Sale;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAllById(Long id, Sort id1);

    List<Sale> findAllByClient_Id(Long clientId, Sort id1);

    List<Sale> findAllByClientId(Long clientID, Sort id);

    List<Sale> findAllByCreatedBy_Id(Long id, Sort id1);

    @Query(
            value = "select * from sale where created_by_id = ?1 AND created_at BETWEEN ?2 AND ?3",
            nativeQuery = true
    )
    List<Sale> selectSalesByPeriod(Long createdById, Timestamp time, Timestamp time1);
}
