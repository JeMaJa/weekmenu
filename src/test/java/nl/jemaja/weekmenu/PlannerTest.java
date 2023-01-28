package nl.jemaja.weekmenu;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.config.DataSetup;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.RecipeStatus;
import nl.jemaja.weekmenu.repository.DayRecipeRepository;
import nl.jemaja.weekmenu.repository.RecipeRepository;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.PlannerService;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.exceptions.NoRecipeFoundException;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Configurable
@SpringBootTest
public class PlannerTest {
	
		@Autowired
		DayRecipeRepository	dRRepo;
		
		@Autowired
		RecipeRepository rRepo;

		@Autowired
		DayRecipeService dRService;
		
		@Autowired
		RecipeService rService;
		
		@Autowired
		PlannerService plannerService;
		
		@Test
		void testNextTenDays() {
			dRRepo.deleteAll();
			rRepo.deleteAll();
			this.setup();
			Calendar cal = Calendar.getInstance();
			
			Date start = new java.sql.Date(cal.getTimeInMillis());
			cal.add(Calendar.DATE, 10);
			Date end = new java.sql.Date(cal.getTimeInMillis());
			
			try {
				plannerService.planPeriod(start, end);
			} catch (NoRecipeFoundException e) {
				// TODO Auto-generated catch block
				log.debug("We have a NoRecipeFoundException");
				e.printStackTrace();
			}

		}
		
		void setup() {
			DataSetup.days(dRService);
			DataSetup.recipes(rService);
			DayRecipe dr1 = new DayRecipe();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -15);
			Date start = new java.sql.Date(cal.getTimeInMillis());
			cal.add(Calendar.DATE, 10);
			Date end = new java.sql.Date(cal.getTimeInMillis());
			try {
				List<DayRecipe> dayRecipeList = dRService.findByDateBetween(start, end);
				DayRecipe dayRecipe = dayRecipeList.get(0);
			
				dayRecipe.setRecipe(rService.findByRecipeName("Pizza"));
				dayRecipe.setStatus(RecipeStatus.SELECTED);	
				dRService.save(dayRecipe);
				
				dayRecipe = dayRecipeList.get(1);
				dayRecipe.setRecipe(rService.findByRecipeName("Red cabbage stew with sausages."));
				dayRecipe.setStatus(RecipeStatus.SELECTED);	
				dRService.save(dayRecipe);
				
				dayRecipe = dayRecipeList.get(2);
				dayRecipe.setRecipe(rService.findByRecipeName("Red cabbage stew with sausages."));
				dayRecipe.setStatus(RecipeStatus.SELECTED);	
				dRService.save(dayRecipe);
				
				dayRecipe = dayRecipeList.get(3);
				dayRecipe.setRecipe(rService.findByRecipeName("Fries"));
				dayRecipe.setStatus(RecipeStatus.SELECTED);	
				dRService.save(dayRecipe);
				
				dayRecipe = dayRecipeList.get(4);
				dayRecipe.setRecipe(rService.findByRecipeName("Fries"));
				dayRecipe.setStatus(RecipeStatus.SELECTED);	
				dRService.save(dayRecipe);
				
				dayRecipe = dayRecipeList.get(5);
				dayRecipe.setRecipe(rService.findByRecipeName("Stew"));
				dayRecipe.setStatus(RecipeStatus.SELECTED);	
				dRService.save(dayRecipe);
				
				dayRecipe = dayRecipeList.get(6);
				dayRecipe.setRecipe(rService.findByRecipeName("Fries"));
				dayRecipe.setStatus(RecipeStatus.SELECTED);	
				dRService.save(dayRecipe);
				
				dayRecipe = dayRecipeList.get(7);
				dayRecipe.setRecipe(rService.findByRecipeName("Vegetarian lasagne"));
				dayRecipe.setStatus(RecipeStatus.SELECTED);	
				dRService.save(dayRecipe);
				
				dayRecipe = dayRecipeList.get(8);
				dayRecipe.setRecipe(rService.findByRecipeName("Pizza"));
				dayRecipe.setStatus(RecipeStatus.SELECTED);	
				dRService.save(dayRecipe);
				
				dayRecipe = dayRecipeList.get(9);
				dayRecipe.setRecipe(rService.findByRecipeName("Vegetarian lasagne"));
				dayRecipe.setStatus(RecipeStatus.SELECTED);	
				dRService.save(dayRecipe);
				
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}


