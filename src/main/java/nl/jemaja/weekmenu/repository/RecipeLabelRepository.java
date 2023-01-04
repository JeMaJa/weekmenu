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

	
	@Query(value="select l from RecipeLabel l where l.recipes= ?1")
	List<RecipeLabel> findByRecipe(long recipeId);

	

}
