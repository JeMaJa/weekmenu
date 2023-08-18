package nl.jemaja.weekmenu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.jemaja.weekmenu.model.Ingredient;
import nl.jemaja.weekmenu.model.IngredientQuantity;
import nl.jemaja.weekmenu.model.IngredientQuantityKey;
import nl.jemaja.weekmenu.model.Recipe;

public interface IngredientQuantityRepository extends JpaRepository<IngredientQuantity, IngredientQuantityKey> {

	

	IngredientQuantity findByRecipeAndIngredient(Recipe recipe, Ingredient ingredient);

	List<IngredientQuantity> findByRecipe(Recipe recipe);

}
