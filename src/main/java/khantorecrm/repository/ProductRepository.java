package khantorecrm.repository;

import khantorecrm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select case when count(c)> 0 then true else false end from khantorecrm.model.Product c where c.name = ?1")
    boolean existsByName(String name);

    @Query("select case when count(c)> 0 then true else false end from khantorecrm.model.Product c where c.name = ?1 and c.id <> ?2")
    boolean existsByNameAndIdIsNot(String name, Long id);

    @Query("select case when count(c)> 0 then true else false end from khantorecrm.model.Product c where c.id = ?1")
    boolean existsById(Long id);
}
