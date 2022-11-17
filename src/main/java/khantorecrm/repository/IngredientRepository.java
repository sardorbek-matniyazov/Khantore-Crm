package khantorecrm.repository;

import khantorecrm.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Query("select case when count(c)> 0 then true else false end from khantorecrm.model.Ingredient c where c.id = ?1")
    boolean existsById(Long id);
}
