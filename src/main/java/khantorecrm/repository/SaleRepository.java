package khantorecrm.repository;

import khantorecrm.model.Sale;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAllById(Long id, Sort id1);

    List<Sale> findAllByClient_Id(Long clientId, Sort id1);

    List<Sale> findAllByClientId(Long clientID, Sort id);

    List<Sale> findAllByCreatedBy_Id(Long id, Sort id1);
}
