package nl.jemaja.weekmenu.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.repository.DayRecipeRepository;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.RecipeService;

@SpringBootTest
class DayRecipeTest {
	
	@Autowired
	DayRecipeService dRService;

	@Autowired
	RecipeService rService;
	
	
	@Test
	void test() {
		
	
		Date date = Date.valueOf("2018-07-24");
		Recipe recipe = Recipe.builder()
						.recipeName("Layervlaai")
						.build();
		
		DayRecipe dr1 = DayRecipe.builder()
						.date(date)
						.recipe(recipe)
						.build();
		Recipe r2 = rService.save(recipe);
		DayRecipe dr2 = dRService.save(dr1);
		String s1 = dr1.toString();
		String s2 = dr2.toString();
		
		System.out.println(s1);
		System.out.println(s2);
		
	}

}
