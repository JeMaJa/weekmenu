package nl.jemaja.weekmenu.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

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
class DayRecipeTest {
	
	@Autowired
	DayRecipeService dRService;
	
	@Autowired
	DayRecipeRepository dayRecipeRepo;

	@Autowired
	RecipeService rService;
	
	/*
	@Test
	void test() {
		
	
		Date date = Date.valueOf("2018-07-26");
		Recipe recipe = Recipe.builder()
						.recipeName("Layervlaai2")
						.build();
		
		DayRecipe dr1 = DayRecipe.builder()
						.date(date)
						.recipe(recipe)
						.build();
		TreeMap<String, Object> r2 = rService.save(recipe);
		DayRecipe dr2 = dRService.save(dr1);
		String s1 = dr1.toString();
		String s2 = dr2.toString();
		
		System.out.println(s1);
		System.out.println(s2);
		
	}
	
	@Test
	void maxDateTest() {
		Date date = Date.valueOf("2018-07-25");
		Recipe recipe = Recipe.builder()
						.recipeName("Layervlaai3")
						.build();
		
		DayRecipe dr1 = DayRecipe.builder()
						.date(date)
						.recipe(recipe)
						.build();
		date = Date.valueOf("2021-09-10");
		DayRecipe dr2 = DayRecipe.builder()
				.date(date)
				.recipe(recipe)
				.build();
		
		TreeMap<String, Object> r = rService.save(recipe);
		dr1 = dRService.save(dr1);
		dr2 = dRService.save(dr2);
		
		Date maxFound = dRService.findMaxDate();
		System.out.println("max found: "+maxFound.toString());
		
	}
	*/
	@Test
	void prevDateTest() {
		
		Date date = Date.valueOf("2021-09-09");
		Recipe recipe = Recipe.builder()
						.recipeName("Layervlaai4")
						.build();
		Recipe recipe1 = Recipe.builder()
						.recipeName("Appeltaart")
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
		recipe.setLastEaten(date);
		
		TreeMap<String, Object> r = rService.save(recipe);
		dr1 = dRService.save(dr1);
		dr2 = dRService.save(dr2);
		
		Date maxFound = dRService.findPrevLastEaten(recipe, date);
		System.out.println("Prevmax found: "+maxFound.toString());
		System.out.println("Last eaten before: "+recipe.getLastEaten());
		DayRecipe dr3 = dRService.findByDate(date).get(0);
		dr3.setRecipe(recipe1);
		//dr1.setRecipe(recipe1); // setting the new recipe
		System.out.println("Last eaten after: "+recipe.getLastEaten());
		
	}
/*	@Test
	void createrTest() {
		int i = 10;
		Date date = Date.valueOf("2018-07-24");
		Recipe recipe = Recipe.builder()
						.recipeName("Layervlaai")
						.build();
		
		DayRecipe dr1 = DayRecipe.builder()
						.date(date)
						.recipe(recipe)
						.build();
		date = Date.valueOf("2021-09-09");
		DayRecipe dr2 = DayRecipe.builder()
				.date(date)
				.recipe(recipe)
				.build();
		
		TreeMap<String, Object> r = rService.save(recipe);
		dr1 = dRService.save(dr1);
		dr2 = dRService.save(dr2);
		Date maxFound = dRService.findMaxDate();
		System.out.println("Max before: "+maxFound.toString());
		int j = dRService.creater(i);
		maxFound = dRService.findMaxDate();
		System.out.println("Max After: "+maxFound.toString());
		
	}
	
	@Test
	void pageTest() {
		Date date = Date.valueOf("2018-07-24");
		Recipe recipe = Recipe.builder()
						.recipeName("Layervlaai")
						.build();
		
		DayRecipe dr1 = DayRecipe.builder()
						.date(date)
						.recipe(recipe)
						.build();
		dRService.creater(70); //create 70 days
		log.debug("Created new DayRecipes.");
		
		System.out.println("PAGE");
		for(int k = 0; k<10;k++) {
			PageRequest sortedByDate = PageRequest.of(k, 7, Sort.by("date").ascending());
			Page<DayRecipe> page = dRService.listDayRecipe(null, null, null, sortedByDate);
			for(int i = 0;i< page.getNumberOfElements();i++) {
				System.out.println("Element: ("+k+","+i+ ")  date:" +page.getContent().get(i).getDate());
			}
			
		}
		
		
	}
	/*
	@Test
	void testYear() {
		//DataSetup ds = new DataSetup();
		//DayRecipeService dRservice = new DayRecipeService();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2022);
		cal.set(Calendar.DAY_OF_YEAR, 1);    
		java.util.Date start =  cal.getTime();
		cal.set(Calendar.YEAR, 2024);
		cal.set(Calendar.DAY_OF_YEAR, 1);    
		java.util.Date end=  cal.getTime();
		try {
			dRService.creater(start, end);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Here");
		}
	}
	
	@Test
	void testAnotherYear() {
		DataSetup.days(dRService);
		DataSetup.recipes(rService);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		Date date = new java.sql.Date(cal.getTimeInMillis());
		
		List<Recipe> recipeList = rService.findLastEaten();

		DayRecipe dr = dRService.findByDate(date).get(0); // this is tomorrows dayRecipe
		
		dRService.scheduleDiner(dr, recipeList.get(0), rService);
		DayRecipe dr2 = dRService.findByDate(date).get(0); // this is tomorrows dayRecipe
		assertEquals(dr2.getRecipe().getRecipeId(),recipeList.get(0).getRecipeId());
		
		
	}
	@Test
	void mapperWithNullTest() {
		Date date = Date.valueOf("2017-03-27");
		DayRecipe dr = DayRecipe.builder()
				.date(date)
				.recipe(null)
				.workday(false)
				.status(0)
				.build();
		DayRecipeMapper mapper = new DayRecipeMapper();
		DayRecipeDto dto = mapper.dayRecipeToDayRecipeDto(dr);
		System.out.println("DTO: "+dto.toString());
		System.out.println("dto mapped back: "+mapper.dayRecipeDtoToDayRecipe(dto).toString());
	}

	@Test
	void getOrCreateTest() {
		Date from = Date.valueOf("2000-01-01");
		Date to = Date.valueOf("2000-01-10");
		Date test = Date.valueOf("2000-01-05");
		DayRecipe dr = DayRecipe.builder()
				.date(test)
				.recipe(null)
				.workday(false)
				.status(0)
				.build();
		dRService.save(dr); // we add one in the middle, to see if this gets found instead of created
		List<DayRecipe> list = dRService.getOrCreate(from, to);
		System.out.println("numer of items: "+list.size());
		System.out.println("the first: "+list.get(0));
		System.out.println("the 5th: "+list.get(4));
		System.out.println("the 10th: "+list.get(9));
	}*/

}
