/**
 * 
 */
package nl.jemaja.weekmenu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.jemaja.weekmenu.model.Complexity;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeList;
import nl.jemaja.weekmenu.repository.RecipeRepository;
import nl.jemaja.weekmenu.service.RecipeService;

/**
 * @author yannick.tollenaere
 *
 */
@RestController
public class RecipeRestController {
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	/*
	 * @GetMapping("/test")
	 
	public String getRecipe(ModelMap model) {
		Recipe rec = Recipe.builder()
					.recipeName("Goulash")
					.build();
		
		try {
			recipeService.save(rec);
		} catch (Exception e) {
			System.out.println("Boeoeoeoeoe");
		}
		Recipe rec2 = Recipe.builder()
				.recipeName("Soep")
				.build();
	
		try {
			recipeService.save(rec);
		} catch (Exception e) {
			System.out.println("Boeoeoeoeoe");
		}
		RecipeList recipeList = new RecipeList();
		//to do, make a query to get all the recipies from the DB and put them in recipeList
		model.put("recipeList", recipeList);
		
		
		
		return "recipe";
		
	}
	*/
	
	@GetMapping(path = "/blaat", produces = MediaType.APPLICATION_JSON_VALUE) 
	public List<Recipe> getRecipes() {
		return recipeRepository.findAll();
	}
	


}
