/**
 * 
 */
package nl.jemaja.weekmenu.controller.api;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.config.DataSetup;
import nl.jemaja.weekmenu.dto.RecipeDto;
import nl.jemaja.weekmenu.dto.mapper.RecipeMapper;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeLabel;
import nl.jemaja.weekmenu.repository.RecipeRepository;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.RecipeLabelService;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yannick.tollenaere
 *
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/recipe")
public class RecipeRestControllerV1 {
	
	@Autowired
	RecipeService recipeService;
	
	@Autowired
	RecipeRepository recipeRepository;
	
	@Autowired
	DayRecipeService dRService;
	
	@Autowired
	RecipeLabelService labelService;

	
	
	
	@GetMapping(path = "/recipe", produces = MediaType.APPLICATION_JSON_VALUE)
	public Recipe getRecipeById(long id) {
		log.debug("returning recipe id: "+id);
		return recipeRepository.findByRecipeId(id);
	}
	
	@GetMapping(path = "/init", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity init() {
		ResponseEntity response = new ResponseEntity(HttpStatus.OK);
		
		try {
			//DataSetup.days(dRService);
			DataSetup.recipes(recipeService);
			//DataSetup.users();
		} catch (Exception e) {
			response.status(HttpStatus.INTERNAL_SERVER_ERROR);
			log.error(e.getStackTrace().toString());
		}
		
		
		return response;
		
	}
	
	@PostMapping(path = "addlabel/{recipeId}/{labelId}")
	public ResponseEntity<RecipeDto> addLabel(@PathVariable("recipeId") long recipeId,@PathVariable("labelId") long labelId) {
		//HttpStatus status = HttpStatus.OK;
		log.debug("in add label");
		
		try {
			Recipe recipe = recipeService.findByRecipeId(recipeId);
			RecipeLabel label = labelService.findById(labelId);
			log.debug("got recipe: "+recipe.getRecipeName()+" and label: " +label.getName());
			recipeService.setLabel(recipe, label);
			RecipeMapper mapper = new RecipeMapper();
			RecipeDto dto = mapper.recipeToRecipeDto(recipe);
			ResponseEntity response = new ResponseEntity(dto, HttpStatus.OK);
			return response;
			
		} catch (NotFoundException e) {
			log.error(e.getMessage());
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	
		
		
	}
	
	@GetMapping(path = "labels/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RecipeLabel>> getRecipeLabels(@PathVariable("recipeId") Long id) {
		try {
			Recipe recipe = recipeService.findByRecipeId(id);
			return new ResponseEntity<List<RecipeLabel>>(recipe.getLabels(),HttpStatus.OK);
		} catch (NotFoundException e) {
			log.debug("Did not find labels for recipe:"+id);
			return new ResponseEntity<List<RecipeLabel>>(HttpStatus.NO_CONTENT);
			
		}  catch (Exception e) {
			log.error("We have an exception in the return new ResponseEntity<List<RecipeLabel>>( method.");
			return new ResponseEntity<List<RecipeLabel>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	


}
