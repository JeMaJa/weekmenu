/**
 * 
 */
package nl.jemaja.weekmenu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;

import java.sql.Date;
import java.util.List;

/**
 * @author yannick.tollenaere
 *
 */
@Transactional
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
	Recipe findByRecipeId(Long recipeId);

	List<Recipe> findByRecipeName(String name);

	@Modifying
	@Query("update Recipe r set r.recipeName =?1, r.externalUrl = ?2, r.description = ?3, r.complexity = ?4, r.workdayOk = ?5, r.healthScore = ?6, r.vega = ?7 where r.recipeId = ?8")
	int update(String recipeName, String externalUrl, String description, int complexity, boolean workdayOk, int healthScore, boolean vega, Long recipeId);

	Page<Recipe> findByRecipeName(String recipeName, PageRequest pageRequest);


	List<Recipe> findAll();

	@Query("select r from Recipe r order by r.lastEaten ASC")
	List<Recipe> findLastEaten();

	@Modifying
	@Query("update Recipe r set r.lastEaten = ?2 where r.recipeId = ?1")
	void updateLastEaten(long recipeId, Date date);

	//Page<Recipe> findAll(PageRequest pageRequest);
	


}
