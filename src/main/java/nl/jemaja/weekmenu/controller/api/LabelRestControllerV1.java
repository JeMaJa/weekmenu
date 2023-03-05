package nl.jemaja.weekmenu.controller.api;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeLabel;
import nl.jemaja.weekmenu.repository.RecipeLabelRepository;
import nl.jemaja.weekmenu.repository.RecipeRepository;
import nl.jemaja.weekmenu.service.RecipeLabelService;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/label/")
public class LabelRestControllerV1 {
	
	@Autowired
	RecipeLabelService rLabelService;
	
	@Autowired
	RecipeLabelRepository rLabelRepo;
	
	@Autowired
	RecipeService recipeService;
	
	@Autowired
	RecipeRepository recipeRepository;


	@GetMapping(path = "label/{labelId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeLabel> getLabel(@PathVariable("labelId") Long id) {
		try {
			RecipeLabel label = rLabelService.findById(id);
			return new ResponseEntity<RecipeLabel>(label, HttpStatus.OK);
		} catch (NotFoundException e) {
			log.debug("Did not find label:"+id);
			return new ResponseEntity<RecipeLabel>(HttpStatus.NO_CONTENT);
			
		}
		
	
	
	}
	@GetMapping(path = "recipelabels/{recipeId}", produces = MediaType.APPLICATION_JSON_VALUE)
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
	
	@PostMapping(path = "createlabel", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeLabel> createLabel(@RequestBody RecipeLabel recipeLabel) {
		ResponseEntity<RecipeLabel> response= (ResponseEntity<RecipeLabel>) new ResponseEntity(HttpStatus.CREATED);
		try {
			String name = recipeLabel.getName();
			if(rLabelService.existsByName(name) == false && name != null && name.length() > 0) {
				rLabelService.save(recipeLabel);
			}
		} catch (Exception e) {
			log.error("We have an exception in the createlabel method.");
			response.status(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
		
	}
}
