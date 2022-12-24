package nl.jemaja.weekmenu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeLabel;

@Repository
public interface RecipeLabelRepository extends JpaRepository<RecipeLabel, Long> {

	RecipeLabel findById(long id);

	//@Query(value = "SELECT l.* FROM recipe_label l, recipe_labels j WHERE j.recipe_id = ?1 AND j.label_id = l.label_id", nativeQuery = true)
	//List<RecipeLabel> findByRecipe(long recipeId);
	
	

}
