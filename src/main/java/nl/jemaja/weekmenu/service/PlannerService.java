/**
 * 
 */
package nl.jemaja.weekmenu.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;

import nl.jemaja.weekmenu.repository.RecipeRepository;

import nl.jemaja.weekmenu.repository.DayRecipeRepository;
import nl.jemaja.weekmenu.model.PlanRule;
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
	

	@Autowired
	RecipeRepository recipeRepo;


	
	
	public DayRecipe longestPlanDay(DayRecipe dayRecipe) throws NoRecipeFoundException {
		Date date = dayRecipe.getDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// Need to add a check to see if there already was something planned, in that case skip the day.
		List<Recipe> recipes = new ArrayList<Recipe>();
		if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && 
				cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			//it is a workday
			log.debug("Finding a recipe for a workday.");
			recipes = rService.findAllWorkdayActive();
		} else {
			recipes = rService.findAllActive();
		}
		if(recipes.isEmpty()) {
			throw new NoRecipeFoundException();
		}
		//Determine last eaten for each recipe
		TreeMap<Date, Recipe> map = new TreeMap<Date, Recipe>();
		for(int i=0;i<recipes.size();i++) {
			Date lastEaten = rService.findLastEaten(recipes.get(i),date);
			map.put(lastEaten, recipes.get(i));
		}
		Date lowest = (Date) map.firstKey();

		Recipe recipe = map.get(lowest);
		log.debug("Previous date: "+ lowest);
		log.debug("recipe: "+recipe.getRecipeName());
		dRService.suggestDiner(dayRecipe, recipe, rService);
		return dayRecipe;


		
	}
	public void planPeriod(Date startDate, Date endDate) throws NoRecipeFoundException {
		planPeriod(startDate,endDate,PlanRule.LONGEST);
	}

	public void planPeriod(Date startDate, Date endDate, PlanRule rule) throws NoRecipeFoundException {
		log.debug("planning: "+startDate.toString() + " - " + endDate.toString());
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		
		List<DayRecipe> dayRecipeList = dRService.findByDateBetween(startDate, endDate);
		DayRecipe dayRecipe = new DayRecipe();
		for(int j=0;j<dayRecipeList.size();j++) {
			
			// Need to add a check to see if there already was something planned, in that case skip the day.
			if(dayRecipeList.get(j).getStatus() == 0 || dayRecipeList.get(j).getRecipe() == null) {
				switch(rule) {
				case LONGEST: {
					dayRecipe = longestPlanDay(dayRecipeList.get(j));
					log.debug("scheduled: "+dayRecipe.getRecipe().getRecipeName()+ " for: "+dayRecipe.getDate());
				}
				
				case TWOWEEKRANDOM: {
					//to do
				}
				case CULINAIR: {
					//to do
				}
				case MEALPREP: {
					//to do
				}
				case LOWCOMPLEX_LONGEST: {
					//to do
				}
				case LOWCOMPLEX_TWOWEEKRANDOM: {
					//to do
				}
				case HEALTHY_LONGEST: {
					//to do
				}
				case HEALTHY_TWOWEEKRANDOM: {
					//to do
				}
				default:
					dayRecipe = longestPlanDay(dayRecipeList.get(j));
					log.debug("scheduled: "+dayRecipe.getRecipe().getRecipeName()+ " for: "+dayRecipe.getDate());
			}
			}
		}
			
		
	}
}
