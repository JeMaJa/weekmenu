/**
 * 
 */
package nl.jemaja.weekmenu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.repository.RecipeRepository;

/**
 * @author yannick.tollenaere
 * Service to get recipes
 */

@Service
public class RecipeService {
	
	@Autowired
	RecipeRepository recipeRepo;
	
	 public Recipe save(Recipe recipe) {
		recipeRepo.save(recipe);
		return recipe;
		
	}
	 public List<Recipe> findByRecipeName(String name) {
		return recipeRepo.findByRecipeName(name);
	}

}
