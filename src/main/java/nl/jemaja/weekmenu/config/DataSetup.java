/**
 * 
 */
package nl.jemaja.weekmenu.config;

import java.util.Calendar;
import java.util.Date;

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
		Date start =  cal.getTime();
		cal.set(Calendar.YEAR, 2024);
		cal.set(Calendar.DAY_OF_YEAR, 1);    
		Date end=  cal.getTime();
		try {
			dRService.creater(start, end);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void recipes(RecipeService recipeService) {
		Recipe r = Recipe.builder()
				.recipeName("Stoofvlees op grootmoeders wijze.")
				.workdayOk(false)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(4)
				.healthScore(1)
				.lastEaten(java.sql.Date.valueOf("2018-07-25"))
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Rodekool Stamppot met worstjes.")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(3)
				.lastEaten(java.sql.Date.valueOf("2018-07-26"))
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
				.lastEaten(java.sql.Date.valueOf("2018-07-27"))
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}

		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Friet met snack")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(1)
				.lastEaten(java.sql.Date.valueOf("2018-07-28"))
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Hutspot")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(4)
				.lastEaten(java.sql.Date.valueOf("2018-07-29"))
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
				.lastEaten(java.sql.Date.valueOf("2018-07-30"))
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Vegetarische lasagne")
				.workdayOk(true)
				.vega(true)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(2)
				.lastEaten(java.sql.Date.valueOf("2018-07-24"))
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
				.complexity(4)
				.healthScore(4)
				.lastEaten(java.sql.Date.valueOf("2018-07-23"))
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());
		r = Recipe.builder()
				.recipeName("Biefstuk met pepersaus en frites")
				.workdayOk(true)
				.vega(false)
				.description("Lorum Ipsum.")
				.complexity(2)
				.healthScore(2)
				.lastEaten(java.sql.Date.valueOf("2018-07-22"))
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
				.lastEaten(java.sql.Date.valueOf("2018-07-21"))
				.build();
		try {
			recipeService.save(r);
		} catch (Exception e){
			//ignoring
		}
		log.debug("Saved recipe: "+r.getRecipeName());



	}
}
