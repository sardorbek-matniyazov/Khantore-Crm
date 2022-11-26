package khantorecrm.repository;

import khantorecrm.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByNameAndPhone(String name, String phone);
}
