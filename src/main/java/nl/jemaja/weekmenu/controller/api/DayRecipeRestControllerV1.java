package nl.jemaja.weekmenu.controller.api;

import java.net.http.HttpResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.dto.DayRecipeDto;
import nl.jemaja.weekmenu.dto.mapper.DayRecipeMapper;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.repository.DayRecipeRepository;

import nl.jemaja.weekmenu.repository.RecipeRepository;

import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.PlannerService;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.exceptions.IncorrectStatusException;
import nl.jemaja.weekmenu.util.exceptions.NoRecipeFoundException;
@Slf4j
@RestController
@RequestMapping("/api/v1/dayrecipe/")
public class DayRecipeRestControllerV1 {

	@Autowired
	DayRecipeService dRService;
	
	@Autowired

	PlannerService plannerService;
	
	@Autowired
	RecipeService recipeService;
	
	@Autowired
	RecipeRepository recipeRepo;

	DayRecipeRepository dayRecipeRepo;
	
	@Autowired
	PlannerService pService;

	
	@GetMapping(path = "dayrecipe/{id}" )
	public ResponseEntity<DayRecipe> getDayRecipe(@PathVariable("id") Long id){
		DayRecipe dayRecipe = dRService.findById(id);
		return new ResponseEntity(dayRecipe, HttpStatus.OK);
	}
	
	@PutMapping(path = "accept/{id}")
	public ResponseEntity<DayRecipeDto> acceptSuggestion(@PathVariable("id") Long id) {
		DayRecipeDto returnVal = null;
		DayRecipeMapper map = new DayRecipeMapper();
		HttpStatus status = HttpStatus.OK;
		try {
			//dRService.acceptSuggestion(id);
			
			DayRecipe dr2 = dRService.findById(id);
			if(dr2.getStatus() == 1)
			{
				dr2.setStatus(2);
				dRService.save(dr2);
			} else {
				log.warn("Day Recipe: "+id+" doe not have status 1, so cannot be updated to 2");
				status = HttpStatus.CONFLICT;
			}
			returnVal = map.dayRecipeToDayRecipeDto(dr2);

		} catch (Exception e) {
			log.error("Ow now!");
			log.error(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
			
		}
		
		return new ResponseEntity<DayRecipeDto>(returnVal, status);
	}
	
	@PostMapping(path = "planperiod")
	public ResponseEntity<String> planperiod(@RequestParam String f, String t) {
		log.debug("API plan Post f: "+ f + " t: "+ t);

		//plan these days.
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();

					java.util.Date tf = sdf.parse(f);
					java.util.Date tt = sdf.parse(t);
					log.debug("temp tf: "+tf.toLocaleString());
					java.sql.Date from = new Date(tf.getTime());
					java.sql.Date to = new Date(tt.getTime());
					log.debug("from: "+from.toLocaleString());
					plannerService.planPeriod(from, to);
					return new ResponseEntity<>("planned: "+f + " "+t,HttpStatus.ACCEPTED);
				} catch (NoRecipeFoundException e) {
					// TODO Auto-generated catch block
					log.warn("No Recipe found exception trown");
					return new ResponseEntity<>("No Recipe found exception trown",HttpStatus.NOT_FOUND);
					
					
				} catch (Exception e) {
					log.error(e.getMessage());
					//response.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
					log.debug("stack"+ e.getStackTrace());
					return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
				}
		
	}
	
	@PutMapping(path = "suggest/{id}")
	public ResponseEntity<DayRecipeDto> suggestNew(@PathVariable("id") Long id) {
		DayRecipeDto returnVal = null;
		DayRecipeMapper map = new DayRecipeMapper();
		HttpStatus status = HttpStatus.OK;
		try {
			DayRecipe dr = dRService.findById(id);
			dr.setRecipe(null);
			dr.setStatus(0);
			Date date = dr.getDate();
			

			//Remove the old recipe so it will replan
			dr.setRecipe(null);
			plannerService.planPeriod(date, date);

			DayRecipe dr2 = dRService.findById(id);
			returnVal = map.dayRecipeToDayRecipeDto(dr2);
		
		} catch (Exception e) {

			log.error("Could not suggest new recipe for DayRecipe with ID: "+id);

			log.error(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
			
		}
		return new ResponseEntity<DayRecipeDto>(returnVal, status);
		
	}
	
	@PostMapping(path = "create/{date}")
	public ResponseEntity<DayRecipeDto> createDayRecipe(@PathVariable("date") String d) {
		//to do, create a DayRecipe for a date
		return null;
	}
	
	


}
	


