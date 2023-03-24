package khantorecrm.repository;

import khantorecrm.model.Client;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByNameAndPhone(String name, String phone);

    @Query(value = "select c from client c join balance b on c.balance=b where b.amount < 0")
    List<Client> findAllByBalance_AmountSmallerThan0();

    List<Client> findAllByCreatedBy_Id(Long id);

    List<Client> findAllByCreatedBy_IdOrCreatedBy_Id(Long id, Long l, Sort id1);
}
