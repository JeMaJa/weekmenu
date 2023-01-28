/**
 * 
 */
package nl.jemaja.weekmenu.repository;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * @author yannick.tollenaere
 *
 */

@Repository
public interface DayRecipeRepository extends PagingAndSortingRepository<DayRecipe, Long> {
	List<DayRecipe> findByDate(Date date);
	
	@Deprecated
	List<DayRecipe> findByDateBetween(Date startDate, Date endDate);
	
	List<DayRecipe> findByDateBetweenOrderByDateAsc(Date startDate, Date endDate);
	
	List<DayRecipe> findByRecipe(Recipe recipe);
	
	@Query(value = "select max(date) from DayRecipe")
	public Date findMaxDate();

	Page<DayRecipe> findByRecipe(Recipe recipe, PageRequest pageRequest);

	Page<DayRecipe> findByDateBetween(Date startDate, Date endDate, PageRequest pageRequest);



	@Query(value = "select max(dr.date) from DayRecipe dr where dr.recipe = ?1 and dr.date < ?2")
	public Date findPrevLastEaten(Recipe recipe, Date date);

	@Query(value = "select min(dr.date) from DayRecipe dr where dr.recipe = ?1 and dr.date >= ?2")
	public Date findNextEaten(Recipe recipe, Date currentdate);

	@Query(value = "select count(dr.date) from DayRecipe dr where dr.recipe = ?1 and dr.date < ?2 and status=2")
	int countEaten(Recipe recipe, Date currentdate);


	

	



	
	
}
