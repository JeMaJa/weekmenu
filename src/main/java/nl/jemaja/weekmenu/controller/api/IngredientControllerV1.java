package nl.jemaja.weekmenu.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.Ingredient;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeLabel;
import nl.jemaja.weekmenu.service.IngredientService;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;

@Slf4j
@RestController
@RequestMapping("/api/v1/ingredient/")
public class IngredientControllerV1 {
	
	@Autowired
	IngredientService iService;
	
	@GetMapping(path = "ingredient/{ingredientId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Ingredient> getIngredient(@PathVariable("ingredientId") Long id) {
		try {
			Ingredient ingredient = iService.findById(id);
			return new ResponseEntity<Ingredient>(ingredient, HttpStatus.OK);
		} catch (NotFoundException e) {
			log.debug("Did not find ingredient:"+id);
			return new ResponseEntity<Ingredient>(HttpStatus.NO_CONTENT);
			
		}
	}
	
	@PostMapping(path = "ingredient", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Ingredient> createLabel(@RequestBody Ingredient ingredient) {
		ResponseEntity<Ingredient> response= new ResponseEntity<Ingredient>(ingredient, HttpStatus.ACCEPTED);
		try {
			String name = ingredient.getName();
			if(iService.existsByName(name) == false && name != null && name.length() > 0) {
				iService.save(ingredient);
			}
		} catch (Exception e) {
			log.error("We have an exception in the createlabel method.");
			response= new ResponseEntity<Ingredient>(ingredient, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
		
	}
	
	@PutMapping(path = "ingredient/{ingredientId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Ingredient> updateLabel(@RequestBody Ingredient ingredient, @PathVariable("ingredientId") Long id) {
		ResponseEntity<Ingredient> response= new ResponseEntity<Ingredient>(ingredient, HttpStatus.OK);
		
		try {
			
			Ingredient ing = iService.findById(id);
			if(ingredient.validate()) {
				ing.update(ingredient);
				iService.save(ing);
				response= new ResponseEntity<Ingredient>(ing, HttpStatus.ACCEPTED);
			} else {
				//label name not valid or already in use
				log.warn("Unable to update recipelabel. Name not valid or already in us.");
				response= new ResponseEntity<Ingredient>(ingredient, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("We have an exception in the updatelabel method.");
			response= new ResponseEntity<Ingredient>(ingredient, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
		
	}
	
	@DeleteMapping(path = "ingredient/{ingredientId}")
	public ResponseEntity deleteLabel(@PathVariable("ingredientId") Long id) {
		ResponseEntity response= new ResponseEntity( HttpStatus.OK);
		
		try {
			//String name = recipeLabel.getName();
			iService.deleteById(id);
		
		} catch (Exception e) {
			log.error("We have an exception in the deletelabel method.");
			response= new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
		
	}

}
