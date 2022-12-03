package khantorecrm.repository;

import khantorecrm.model.ItemForCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface IngredientRepository extends JpaRepository<ItemForCollection, Long> {
    boolean existsById(Long id);

    @Query(
            value = "delete from items_for_collection where product_fk = ?1",
            nativeQuery = true
    )
    Integer deleteAllByProductId(Long id);
}
