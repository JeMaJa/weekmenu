/**
 * 
 */
package nl.jemaja.weekmenu.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.jemaja.weekmenu.model.Recipe;

/**
 * @author yannick.tollenaere
 *
 */
class RecipeTest {

	@Test
	void RecipeIngredienttest() {
		
		Recipe recipe = Recipe.builder()
						.recipeName("test recipe1")
						.build();
		
		
		//fail("Not yet implemented");
	}

}
