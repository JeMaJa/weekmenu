package nl.jemaja.weekmenu.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.Ingredient;
import nl.jemaja.weekmenu.model.IngredientQuantity;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.service.IngredientQuantityService;
import nl.jemaja.weekmenu.service.IngredientService;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;

@Slf4j
@RestController
@RequestMapping("/api/v1/ingredientquantity/")
public class IngredientQuantityControllerV1 {
	
	@Autowired
	IngredientQuantityService iQService;
	
	@Autowired
	IngredientService iService;
	
	@Autowired
	RecipeService rService;
	
	@GetMapping(path = "{recipeId}/{ingredientId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IngredientQuantity> getIngredient(@PathVariable("ingredientId") Long ingredientId, @PathVariable("recipeId") Long recipeId) {
		try {
			Ingredient ingredient = iService.findById(ingredientId);
			Recipe recipe = rService.findByRecipeId(recipeId);
			IngredientQuantity ingredientQuantity = iQService.findByRecipeAndIngredient(recipe,ingredient);
			
			return new ResponseEntity<IngredientQuantity>(ingredientQuantity, HttpStatus.OK);
		} catch (NotFoundException e) {
			log.debug("Did not find ingredientQuantity for recipe:"+recipeId + " with id: "+ingredientId);
			return new ResponseEntity<IngredientQuantity>(HttpStatus.NO_CONTENT);
			
		}
	}
	
	

}
