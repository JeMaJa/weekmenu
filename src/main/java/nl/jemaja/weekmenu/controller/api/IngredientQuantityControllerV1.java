package nl.jemaja.weekmenu.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.dto.IQDto;
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
	public ResponseEntity<IngredientQuantity> getIngredient(
			@PathVariable("ingredientId") Long ingredientId, 
			@PathVariable("recipeId") Long recipeId) {
		try {
			Ingredient ingredient = iService.findById(ingredientId);
			Recipe recipe = rService.findByRecipeId(recipeId);
			IngredientQuantity ingredientQuantity = iQService.findByRecipeAndIngredient(recipe, ingredient);
			
			if(ingredientQuantity == null) {
				log.debug("No ingredientQuantity found for recipe: {} and ingredient: {}", recipeId, ingredientId);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			return new ResponseEntity<>(ingredientQuantity, HttpStatus.OK);
		} catch (NotFoundException e) {
			log.error("Ingredient or Recipe not found - ingredientId: {}, recipeId: {}", ingredientId, recipeId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error("Error retrieving ingredient quantity", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Add or update an ingredient quantity for a recipe.
	 * If the ingredient already exists in the recipe, updates the quantity.
	 * If the ingredient doesn't exist, creates a new entry.
	 * 
	 * @param iQuantity DTO containing ingredientId, recipeId, and quantity
	 * @return ResponseEntity with the result
	 */
	@PostMapping(path = "")
	public ResponseEntity<IQDto> addIngredientQuantity(@RequestBody IQDto iQuantity) {
		log.debug("Received request to add/update ingredient quantity: {}", iQuantity);
		
		// Validation: Check for required fields
		if (iQuantity.getIngredientId() <= 0 || iQuantity.getRecipeId() <= 0) {
			log.warn("Invalid ingredientId or recipeId in request: {}", iQuantity);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// Validation: Quantity must be positive
		if (iQuantity.getQuantity() <= 0) {
			log.warn("Invalid quantity (must be > 0): {}", iQuantity.getQuantity());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		try {
			// Find the ingredient and recipe
			Ingredient ingredient = iService.findById(iQuantity.getIngredientId());
			Recipe recipe = rService.findByRecipeId(iQuantity.getRecipeId());
			
			// Check if this ingredient is already in the recipe
			IngredientQuantity existing = iQService.findByRecipeAndIngredient(recipe, ingredient);
			
			if (existing == null) {
				// CREATE NEW: Ingredient doesn't exist in this recipe yet
				log.info("Creating new ingredient quantity - Recipe: '{}', Ingredient: '{}', Quantity: {}", 
						recipe.getRecipeName(), ingredient.getName(), iQuantity.getQuantity());
				
				IngredientQuantity iQ = new IngredientQuantity();
				iQ.setIngredient(ingredient);
				iQ.setRecipe(recipe);
				iQ.setQuantity(iQuantity.getQuantity());
				iQService.save(iQ);
				
				log.info("Successfully created ingredient quantity for recipe: {}", recipe.getRecipeName());
				return new ResponseEntity<>(iQuantity, HttpStatus.CREATED);
				
			} else {
				// UPDATE EXISTING: Ingredient already exists, update the quantity
				log.info("Updating existing ingredient quantity - Recipe: '{}', Ingredient: '{}', Old Quantity: {}, New Quantity: {}", 
						recipe.getRecipeName(), ingredient.getName(), existing.getQuantity(), iQuantity.getQuantity());
				
				existing.setQuantity(iQuantity.getQuantity());
				iQService.save(existing);
				
				log.info("Successfully updated ingredient quantity for recipe: {}", recipe.getRecipeName());
				return new ResponseEntity<>(iQuantity, HttpStatus.OK);
			}
			
		} catch (NotFoundException e) {
			log.error("Ingredient or Recipe not found - ingredientId: {}, recipeId: {}", 
					iQuantity.getIngredientId(), iQuantity.getRecipeId());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		} catch (Exception e) {
			log.error("Unexpected error while adding/updating ingredient quantity", e);
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    /**
     * Delete an ingredient from a recipe.
     * Removes the IngredientQuantity relationship between the recipe and ingredient.
     *
     * @param recipeId The ID of the recipe
     * @param ingredientId The ID of the ingredient to remove
     * @return ResponseEntity with the result (204 No Content on success)
     */
    @DeleteMapping(path = "{recipeId}/{ingredientId}")
    public ResponseEntity<Void> deleteIngredientQuantity(
            @PathVariable("recipeId") Long recipeId,
            @PathVariable("ingredientId") Long ingredientId) {

        log.debug("Received request to delete ingredient {} from recipe {}", ingredientId, recipeId);

        // Validation: Check for valid IDs
        if (ingredientId <= 0 || recipeId <= 0) {
            log.warn("Invalid ingredientId or recipeId - ingredientId: {}, recipeId: {}", ingredientId, recipeId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            // Find the ingredient and recipe
            Ingredient ingredient = iService.findById(ingredientId);
            Recipe recipe = rService.findByRecipeId(recipeId);

            // Find the IngredientQuantity to delete
            IngredientQuantity ingredientQuantity = iQService.findByRecipeAndIngredient(recipe, ingredient);

            if (ingredientQuantity == null) {
                // Ingredient is not in this recipe
                log.warn("Ingredient {} not found in recipe {} - nothing to delete",
                        ingredient.getName(), recipe.getRecipeName());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Delete the ingredient from the recipe
            log.info("Deleting ingredient '{}' (quantity: {}) from recipe '{}'",
                    ingredient.getName(), ingredientQuantity.getQuantity(), recipe.getRecipeName());

            iQService.delete(ingredientQuantity);

            log.info("Successfully deleted ingredient '{}' from recipe '{}'",
                    ingredient.getName(), recipe.getRecipeName());

            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content - successful deletion

        } catch (NotFoundException e) {
            log.error("Ingredient or Recipe not found - ingredientId: {}, recipeId: {}", ingredientId, recipeId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            log.error("Unexpected error while deleting ingredient quantity", e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
