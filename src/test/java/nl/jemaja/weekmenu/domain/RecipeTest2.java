package nl.jemaja.weekmenu.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;
import java.lang.Short;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.config.DataSetup;
import nl.jemaja.weekmenu.dto.DayRecipeDto;
import nl.jemaja.weekmenu.dto.mapper.DayRecipeMapper;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.repository.DayRecipeRepository;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.RecipeService;
@Slf4j
@Configurable
@SpringBootTest
class RecipeTest2 {

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
	
/*	@Test
	void duplicateTest() {
		
			Recipe recipe = Recipe.builder()
					.recipeName("test recipe123")
					.description("De beschrijving")
					.build();
			recipeService.save(recipe);
			Recipe recipe2 = Recipe.builder()
					.recipeName("test recipe123")
					.description("De andere beschrijving")
					.build();
			TreeMap<String, Object> recipeTree = recipeService.save(recipe);
			Recipe recipe3 = (Recipe) recipeTree.get("Recipe");
			assertEquals(recipe,recipe3);
		
	}*/
	
	
	
	@Test
	void mapperTest() {
		RecipeMapper mapper = new RecipeMapper();
		Recipe rec = Recipe.builder()
						.recipeName("qwerty")
						.description("Een lang\n verhaal")
						.build();
		
		RecipeDto dto = mapper.recipeToRecipeDto(rec);
		
		Recipe rec2 = mapper.recipeDtoToRecipe(dto);
		assertEquals(rec.getRecipeName(),rec2.getRecipeName());
		
	}
	
	@Test
	void lastTest() {
		
		//DataSetup.recipes(recipeService);

		
		Date date = Date.valueOf("2021-09-09");
		Recipe recipe = Recipe.builder()
						.recipeName("Layervlaai4")
						.build();
		DayRecipe dr1 = DayRecipe.builder()
						.date(date)
						.recipe(recipe)
						.status(2)
						.build();
		Date date2 = Date.valueOf("2021-09-08");
		DayRecipe dr2 = DayRecipe.builder()
						.date(date2)
						.recipe(recipe)
						.status(2)
						.build();
		
		Date date3 = recipeService.findLastEaten(recipe);
		System.out.println("Longest not eaten: "+date3.toString());
	}
	
	@Test
	void lastPageTest(){
		DataSetup.recipes(recipeService);
		Pageable sortedLastEatenAsc = 
				  PageRequest.of(0, 5, Sort.by("lastEaten").ascending());
		Page<Recipe> page = recipeService.findLastEatenPage(sortedLastEatenAsc);
		for(int i=0;i<5;i++) {
			System.out.println(i+ "recipe on Page:" +page.getContent().get(i).getRecipeName());
		}
		
	}


}
