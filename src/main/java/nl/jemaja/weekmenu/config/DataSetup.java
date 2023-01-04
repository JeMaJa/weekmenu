/**
 * 
 */
package nl.jemaja.weekmenu.config;

import java.util.Calendar;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.Recipe;

import nl.jemaja.weekmenu.repository.DayRecipeRepository;

import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.RecipeService;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author yannick.tollenaere
 * Class to setup some basic data
 */


@Slf4j
public class DataSetup {



	
	public static void days(DayRecipeService dRService) {
		//create the entire year, and next of DayRecipe objects
		//DayRecipeService dRservice = new DayRecipeService();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2022);
		cal.set(Calendar.DAY_OF_YEAR, 1);    
		Date start =  new Date(cal.getTimeInMillis());
		cal.set(Calendar.YEAR, 2024);
		cal.set(Calendar.DAY_OF_YEAR, 1);    
		Date end=  new Date(cal.getTimeInMillis());
		try {
			dRService.creater(start, end);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void recipes(RecipeService recipeService) {
		Recipe r = Recipe.builder()
				.recipeName("Grandma's stew.")
				.workdayOk(false)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(4)
				.healthScore(1)
				.active(true)
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Red cabbage stew with sausages.")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(3)
				.active(true)
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Lasagne bolognese")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(2)
				.active(true)
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}

		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Fries")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(1)
				.active(true)
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Stew")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(4)
				.active(true)
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Peas and Carrots with meat and potatoes.")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(2)
				.active(true)
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Vegetarian lasagne")
				.workdayOk(true)
				.vega(true)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(2)
				.active(true)
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Pizza")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(1)
				.healthScore(1)
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Steak with pepper sauce and fries")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(2)
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Slow cooked spareribs")
				.workdayOk(false)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(4)
				.healthScore(2)
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());



	}
	
}
