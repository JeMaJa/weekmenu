/**
 * 
 */
package nl.jemaja.weekmenu.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.dto.DayRecipeDto;
import nl.jemaja.weekmenu.dto.InfoDto;
import nl.jemaja.weekmenu.dto.mapper.DayRecipeMapper;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.PlannerService;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.exceptions.NoRecipeFoundException;

/**
 * @author yannick.tollenaere
 * 
 * Controller that shows some periods and the planned dinner.
 *
 */
@Slf4j
@Controller
public class MenuController {
	
	@Autowired
	PlannerService plannerService;
	
	@Autowired
	RecipeService rService;
	
	@Autowired
	DayRecipeService dRService;
	
	
	@GetMapping(path = "/")
	String showPeriod(ModelMap model, Date from, Date to) {
		Calendar cal = Calendar.getInstance();
		InfoDto infoDto = new InfoDto();
		List<DayRecipeDto> dayRecipeDtos = new ArrayList<DayRecipeDto>();
		DayRecipeMapper mapper = new DayRecipeMapper();
		
		if(from == null) {
			from = new Date(cal.getTimeInMillis());
		}
		if(to == null) {
			cal.add(Calendar.DATE, 14);
			to = new Date(cal.getTimeInMillis());
		}
		
		//check if DayRecipe objects exist, if not make them probably not needed to fetch, since we will fetch again after planning
		List<DayRecipe> dayRecipeList = dRService.getOrCreate(from,to);
		
		//plan these days.
		try {
			plannerService.planPeriod(from, to);
		} catch (NoRecipeFoundException e) {
			// TODO Auto-generated catch block
			log.error("No Recipe found exception trown");
			
			e.printStackTrace();
			infoDto.setType("Error");
			infoDto.setBody("No Recipe's found in the database. Add at least one recipe to schedule a menu.");
		}
		dayRecipeList.clear(); // clear the list
		dayRecipeList = dRService.findByDateBetween(from, to); // now we have the planned versions
		
		for(int i =0; i<dayRecipeList.size();i++) {
			dayRecipeDtos.add(mapper.dayRecipeToDayRecipeDto(dayRecipeList.get(i)));
		}
		model.put("dayRecipeDtos", dayRecipeDtos);
		model.put("infoDto", infoDto);
		
		return "period";
	}
	
	

}
