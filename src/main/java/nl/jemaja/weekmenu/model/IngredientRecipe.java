/**
 * 
 */
package nl.jemaja.weekmenu.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yannick.tollenaere
 *
 *links recipies to ingredients
 */
@Entity


public class IngredientRecipe {
	@Id
    @GeneratedValue
	private long IngredientRecipeId;
	private long RecipeId;
	private long IngredientId;
	
	public IngredientRecipe(long recipeId, long ingredientId) {
		
		/*
		 * Add verification to see if Recipe and ingredient exist
		 */
		
		super();
		RecipeId = recipeId;
		IngredientId = ingredientId;
	}
	
	

}
