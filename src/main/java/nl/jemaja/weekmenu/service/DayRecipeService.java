package nl.jemaja.weekmenu.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.repository.DayRecipeRepository;

@Service
public class DayRecipeService {
	
	@Autowired
	DayRecipeRepository dayRecipeRepo;
	
	public DayRecipe save(DayRecipe dayRecipe) {
		dayRecipeRepo.save(dayRecipe);
		return dayRecipe;
		
	}
	List<DayRecipe> findByDate(Date date) {
		return dayRecipeRepo.findByDate(date);
	}
	
	List<DayRecipe> findByDateBetween(Date startDate, Date endDate) {
		return dayRecipeRepo.findByDateBetween(startDate, endDate);
	}
	
	List<DayRecipe> findByrecipe(Recipe recipe) {
		return dayRecipeRepo.findByRecipe(recipe);
	}

}
