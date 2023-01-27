/**
 * 
 */
package nl.jemaja.weekmenu.util;

import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yannick.tollenaere
 * Creates a bunch of testdata
 */
public class TestDataRunner {
	
	@Autowired
	RecipeService rService;
	
	@Autowired
	DayRecipeService dRService;
	
	public static void TestDataRunner() {
		Recipe R1 = Recipe.builder()
					.recipeName("Stoofvlees op grootmoeders wijze")
					.vega(false)
					.complexity(1)
					.workdayOk(false)
					.healthScore(1)
					.build();
	}
	
	

}
