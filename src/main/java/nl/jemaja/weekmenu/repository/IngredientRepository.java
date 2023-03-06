package nl.jemaja.weekmenu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.jemaja.weekmenu.model.Ingredient;


@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

	List<Ingredient> findByName(String name);

}
