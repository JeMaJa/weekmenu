/**
 * 
 */
package nl.jemaja.weekmenu.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import nl.jemaja.weekmenu.dto.RecipeDto;
import nl.jemaja.weekmenu.dto.mapper.RecipeMapper;
import nl.jemaja.weekmenu.model.Complexity;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.HealthScore;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.TestDataRunner;

/**
 * @author yannick.tollenaere
 *
 */
@SpringBootTest
//@ExtendWith(TestDataRunner.class)
class RecipeTest {
	@MockBean
	static 
	RecipeService rService;
	@Autowired
	RecipeService recipeService;
	
	//@Autowired
	//RecipeMapper mapper;


	@Test
	void RecipeTest() {
		
		Recipe recipe = Recipe.builder()
						.recipeName("test recipe1")
						.description("De beschrijving")
						.createdDate(OffsetDateTime.now())
						.build();
		
		System.out.println(recipe.toString());
		//fail("Not yet implemented");
	}
	
	@Test
	void RecipeSaveTest() {
		
		Recipe recipe = Recipe.builder()
				.recipeName("Nacho soup")
				.description("De beschrijving")
				.createdDate(OffsetDateTime.now())
				.build();
		Recipe recipe2 = Recipe.builder()
				.recipeName("Nacho soup")
				.description("De beschrijving")
				.createdDate(OffsetDateTime.now())
				.build();
		
		
		
	}
	
	@Test
	void RecipeDayTest() {
		Recipe recipe = Recipe.builder()
				.recipeName("test recipe12")
				.description("De beschrijving2")
				.createdDate(OffsetDateTime.now())
				.build();
		
		Recipe recipe2 = Recipe.builder()
				.recipeName("test recipe123")
				.description("De beschrijving23")
				.createdDate(OffsetDateTime.now())
				.build();
		
		java.util.Date date = new java.util.Date();
		java.sql.Date sqlDate = new Date(date.getTime());
		
		DayRecipe dayRecipe = DayRecipe.builder()
							.date(sqlDate)
							.recipe(recipe)
							.build();
		
		System.out.println(dayRecipe.toString());
		System.out.println(recipe.toString());
		System.out.println(recipe2.toString());
	}
	
	@Test
	void findStoofvlees() {
		
		TestDataRunner();
		
		List<Recipe> recList = recipeService.findByRecipeName("Stoofvlees op grootmoeders wijze");
		
		//Recipe rec = recList.get(0);
		System.out.println(recList.toString());
		
	}
	
	@Test
	void mapperTest() {
		RecipeMapper mapper = new RecipeMapper();
		Recipe rec = Recipe.builder()
						.recipeName("qwerty")
						.description("Een lang\n verhaal")
						.build();
		
		RecipeDto dto = mapper.recipeToRecipeDto(rec);
		System.out.println("mappertest: "+dto.getRecipeName());
		
	}

	public void TestDataRunner() {
		
		//RecipeService rs = new RecipeService();
		
		Recipe R1 = Recipe.builder()
					.recipeName("Stoofvlees op grootmoeders wijze")
					.vega(false)
					.complexity(Complexity.COMPLEX)
					.workdayOk(false)
					.healthScore(HealthScore.D)
					.build();
		try {
			System.out.println("Going to save");
			R1 = recipeService.save(R1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
