/**
 * 
 */
package nl.jemaja.weekmenu.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;

/**
 * @author yannick.tollenaere
 *
 */
//@Transactional(isolation = Isolation.SERIALIZABLE)
@Repository
public interface DayRecipeRepository extends PagingAndSortingRepository<DayRecipe, Long> {
	List<DayRecipe> findByDate(Date date);
	
	List<DayRecipe> findByDateBetween(Date startDate, Date endDate);
	
	List<DayRecipe> findByRecipe(Recipe recipe);
	
	@Query(value = "select max(date) from DayRecipe")
	public Date findMaxDate();

	Page<DayRecipe> findByRecipe(Recipe recipe, PageRequest pageRequest);

	Page<DayRecipe> findByDateBetween(Date startDate, Date endDate, PageRequest pageRequest);



	/*
	 * 
	 * To be removed, not used anymore since close of #15
	 * leave code here for learning purposes
	@Transactional(isolation = Isolation.SERIALIZABLE)
	@Modifying
	@Query("update DayRecipe dr set dr.recipe = ?2, dr.status= ?3 where dr.id = ?1")
	public void scheduleDinner(long id, Recipe recipe, int status);
	
	@Transactional(isolation = Isolation.SERIALIZABLE)
	@Modifying
	@Query("update DayRecipe dr set  dr.status= 2 where dr.id = ?1")
	public void acceptSuggestion(long id);
	*/
	


	
	
}
