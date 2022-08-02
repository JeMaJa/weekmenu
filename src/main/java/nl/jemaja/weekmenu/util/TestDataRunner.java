/**
 * 
 */
package nl.jemaja.weekmenu.util;

import org.springframework.beans.factory.annotation.Autowired;

import nl.jemaja.weekmenu.model.Complexity;
import nl.jemaja.weekmenu.model.HealthScore;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.RecipeService;

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
					.complexity(Complexity.COMPLEX)
					.workdayOk(false)
					.healthScore(HealthScore.D)
					.build();
	}
	
	

}
