package nl.jemaja.weekmenu.controller.api;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.dto.DayRecipeDto;
import nl.jemaja.weekmenu.dto.mapper.DayRecipeMapper;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.PlannerService;
import nl.jemaja.weekmenu.util.exceptions.IncorrectStatusException;
@Slf4j
@RestController
@RequestMapping("/api/v1/dayrecipe/")
public class DayRecipeRestControllerV1 {

	@Autowired
	DayRecipeService dRService;
	
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
	
	@PutMapping(path = "suggest/{id}")
	public ResponseEntity<DayRecipeDto> suggestNew(@PathVariable("id") Long id) {
		DayRecipeDto returnVal = null;
		DayRecipeMapper map = new DayRecipeMapper();
		HttpStatus status = HttpStatus.OK;
		try {
			DayRecipe dr = dRService.findById(id);
			Date date = dr.getDate();
			PlannerService pService = new PlannerService();
			pService.planPeriod(date, date);
			DayRecipe dr2 = dRService.findById(id);
			returnVal = map.dayRecipeToDayRecipeDto(dr2);
		
		} catch (Exception e) {
			log.error("Could not accept day Recipe with ID: "+id);
			log.error(e.getMessage());
			status = HttpStatus.BAD_REQUEST;
			
		}
		return new ResponseEntity<DayRecipeDto>(returnVal, status);
		
	}


}
	


