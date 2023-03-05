package nl.jemaja.weekmenu.repository;

import nl.jemaja.weekmenu.model.RecipeLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeLabelRepository extends JpaRepository<RecipeLabel, Long> {

	RecipeLabel findById(long id);

	
	@Query(value="select l from RecipeLabel l where l.recipes= ?1")
	List<RecipeLabel> findByRecipe(long recipeId);


	List<RecipeLabel> findByName(String name);

	

}
