package nl.jemaja.weekmenu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.jemaja.weekmenu.model.Ingredient;


@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByName(String name);

    List<Ingredient> findAllByOrderByNameAsc();

    /**
     * Find ingredient by name (case-insensitive)
     * Used for duplicate detection
     *
     * @param name The ingredient name to search for
     * @return List of ingredients matching the name (case-insensitive)
     */
    @Query("SELECT i FROM Ingredient i WHERE LOWER(i.name) = LOWER(:name)")
    List<Ingredient> findByNameIgnoreCase(@Param("name") String name);

}