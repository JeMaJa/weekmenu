/**
 * 
 */
package nl.jemaja.weekmenu.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.repository.DayRecipeRepository;
import nl.jemaja.weekmenu.util.exceptions.NoRecipeFoundException;

/**
 * @author yannick.tollenaere
 *
 */
@Slf4j
@Service
public class PlannerService {

	@Autowired
	RecipeService rService;

	@Autowired
	DayRecipeService dRService;
	

	

	public void planPeriod(Date startDate, Date endDate) throws NoRecipeFoundException {
		log.debug("planning: "+startDate.toString() + " - " + endDate.toString());
		ArrayList<Long> usedRecipes = new ArrayList<Long>();

		Calendar c = Calendar.getInstance();
		c.setTime(startDate);

		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		List<Recipe> recipeList = rService.findLastEaten();
		if(recipeList.isEmpty()) {
			log.warn("No Recipes found");
			throw new NoRecipeFoundException();
		}
		List<DayRecipe> dayRecipeList = dRService.findByDateBetween(startDate, endDate);
		int i =0;
		int reset=0;
		for(int j=0;j<dayRecipeList.size();j++) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dayRecipeList.get(j).getDate());
			// Need to add a check to see if there already was something planned, in that case skip the day.
			if(dayRecipeList.get(j).getStatus() == 0 || dayRecipeList.get(j).getRecipe() == null) {
			
			reset = 0;
			int k=0;
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && 
					cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				//it is a workday
				log.debug("Finding a recipe for a workday.");

				while(usedRecipes.contains(recipeList.get(k).getRecipeId()) || !recipeList.get(k).isWorkdayOk()) {
					k++;
					if(k>= recipeList.size()) {
						log.debug("All workday recipes already used. Reset and re-shuffle");
						usedRecipes.clear();
						k=0;
						reset++;
						if(reset >1)
						{
							log.debug("We did a second reset for this day, which means there really is no recipe");
							throw new NoRecipeFoundException();
						}
					}
				}
				//we left the while loop, so recipe k must be our recipe
				dRService.suggestDiner(dayRecipeList.get(j), recipeList.get(k), rService);
				usedRecipes.add(recipeList.get(k).getRecipeId()); // adding to the used recipe list

			} else {
				log.debug("finding a recipe for a weekend day");
				while(usedRecipes.contains(recipeList.get(k).getRecipeId())) {
					k++;
					if(k>= recipeList.size()) {
						log.debug("All workday recipes already used. Reset and re-shuffle");
						usedRecipes.clear();
						k=0;
						reset++;
						if(reset >1)
						{
							log.debug("We did a second reset for this day, which means there really is no recipe");
							throw new NoRecipeFoundException();
						}
					}
				}//we left the while loop, so recipe k must be our recipe
				dRService.suggestDiner(dayRecipeList.get(j), recipeList.get(k), rService);
				usedRecipes.add(recipeList.get(k).getRecipeId()); // adding to the used recipe list
			} 
			} else {
				log.debug("For this DayRecipe we already had a recipe scheduled. "+dayRecipeList.get(j).getDate());
			}
			

			


		}





		
	}
}
