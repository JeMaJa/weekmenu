/**
 * 
 */
package nl.jemaja.weekmenu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.jemaja.weekmenu.model.Recipe;
import java.util.List;

/**
 * @author yannick.tollenaere
 *
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
	Recipe findByRecipeId(Long recipeId);

	List<Recipe> findByRecipeName(String name);

	

}
