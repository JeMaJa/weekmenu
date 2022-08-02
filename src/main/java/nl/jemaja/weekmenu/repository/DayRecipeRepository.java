/**
 * 
 */
package nl.jemaja.weekmenu.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;

/**
 * @author yannick.tollenaere
 *
 */
@Repository
public interface DayRecipeRepository extends JpaRepository<DayRecipe, Long> {
	List<DayRecipe> findByDate(Date date);
	
	List<DayRecipe> findByDateBetween(Date startDate, Date endDate);
	
	List<DayRecipe> findByRecipe(Recipe recipe);


	
	
}
