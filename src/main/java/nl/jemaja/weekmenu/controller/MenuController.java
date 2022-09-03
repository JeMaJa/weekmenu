/**
 * 
 */
package nl.jemaja.weekmenu.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.dto.DayRecipeDto;
import nl.jemaja.weekmenu.dto.InfoDto;
import nl.jemaja.weekmenu.dto.RecipeDto;
import nl.jemaja.weekmenu.dto.mapper.DayRecipeMapper;
import nl.jemaja.weekmenu.dto.mapper.RecipeMapper;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.repository.DayRecipeRepository;
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
	
	@Autowired
	DayRecipeRepository dRRepo;
	
	
	@GetMapping(path = "/")
	String showPeriod(ModelMap model, Optional<String> f, Optional<String> t, HttpServletRequest request) throws ParseException {
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		Date from = new Date(0);
	
		Date to =new Date(0);
		
		
		
		Calendar cal = Calendar.getInstance();
		InfoDto infoDto = new InfoDto();
		List<DayRecipeDto> dayRecipeDtos = new ArrayList<DayRecipeDto>();
		//Date from = new Date(0);
		//Date to = new Date(0);
		 Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
	      if (inputFlashMap != null) {
	    	  log.debug("Receive inputflashmap");
	          infoDto = (InfoDto) inputFlashMap.get("updateInfoDto");
	      }
		DayRecipeMapper mapper = new DayRecipeMapper();
		
		if(!f.isPresent()) {
			from = new Date(cal.getTimeInMillis());
		} else {
			from = Date.valueOf(f.get());
		}
		if(!t.isPresent()) {
			cal.add(Calendar.DATE, 14);
			to = new Date(cal.getTimeInMillis());
		} else {
				to = Date.valueOf(t.get());
		}
			
		
		//check if DayRecipe objects exist, if not make them.
		dRService.creater(from,to);
		
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
		//dayRecipeList.clear(); // clear the list
		List<DayRecipe> dayRecipeList = dRService.findByDateBetween(from, to); // now we have the planned versions
		
		for(int i =0; i<dayRecipeList.size();i++) {
			dayRecipeDtos.add(mapper.dayRecipeToDayRecipeDto(dayRecipeList.get(i)));
		}
		List<RecipeDto> recipeDtos = new ArrayList<RecipeDto>();
		List<Recipe> recipeList = rService.findAll();
		RecipeMapper rMapper = new RecipeMapper();
		
		for(int i =0;i<recipeList.size();i++) {
			recipeDtos.add(rMapper.recipeToRecipeDto(recipeList.get(i)));
		}
		
		
		model.put("recipeDtos", recipeDtos);
		
		model.put("dayRecipeDtos", dayRecipeDtos);
		model.put("infoDto", infoDto);
		
		return "period";
	}
	
	@PostMapping(path = "/updatedayrecipe")
	public RedirectView submitPost(
			  HttpServletRequest request, 
			  @ModelAttribute DayRecipeDto dayRecipeDto, 
			  RedirectAttributes redirectAttributes) {
				
		DayRecipeMapper mapper = new DayRecipeMapper();
		Recipe recipe = new Recipe();
		DayRecipe dayRecipe = new DayRecipe();
		InfoDto infoDto = new InfoDto();
		infoDto.setBody("Could not update recipe for: "+dayRecipeDto.getDate());
		infoDto.setType("Warning");
		log.debug("updatedayrecipe got recipe: "+dayRecipeDto.toString());
		try {
			List<DayRecipe> dayRecipeList = dRService.findByDate(dayRecipeDto.getDate());
			dayRecipe = dayRecipeList.get(0);
			try {
				recipe = rService.findByRecipeId(dayRecipeDto.getRecipeId());
				try {
					dRService.scheduleDiner(dayRecipe, recipe, rService);
					infoDto.setBody("Updated menu for: "+dayRecipeDto.getDate());
					infoDto.setType("Success");
				} catch (Exception e) {
					log.error("Could not Schedule dinner");
				}
			} catch (Exception e) {
				log.error("updatedayrecipe did not find a recipe with id "+dayRecipeDto.getRecipeId());
			}
		} catch (Exception e) {
			log.error("updatedayrecipe did not find a day recipe for the provided date: "+dayRecipeDto.getDate());
		}
		
		
		
		
		redirectAttributes.addFlashAttribute("updateInfoDto", infoDto);
		return new RedirectView("/", true);
		
	}
	
	
	
	

}
