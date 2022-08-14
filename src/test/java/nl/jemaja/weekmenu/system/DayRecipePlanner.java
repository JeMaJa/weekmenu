package nl.jemaja.weekmenu.system;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.config.DataSetup;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.PlannerService;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.exceptions.NoRecipeFoundException;
@Slf4j
@SpringBootTest
class DayRecipePlanner {

	@Autowired
	DayRecipeService dRService;
	
	@Autowired
	RecipeService rService;
	
	@Autowired
	PlannerService plannerService;
	
	@Test
	void testNextTenDays() {
		
		DataSetup.days(dRService);
		DataSetup.recipes(rService);
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

}
