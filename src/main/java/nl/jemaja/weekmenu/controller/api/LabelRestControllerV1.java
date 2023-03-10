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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	//move
	
	
	@PostMapping(path = "label", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeLabel> createLabel(@RequestBody RecipeLabel recipeLabel) {
		ResponseEntity<RecipeLabel> response= new ResponseEntity<RecipeLabel>(recipeLabel, HttpStatus.ACCEPTED);
		try {
			String name = recipeLabel.getName();
			if(rLabelService.existsByName(name) == false && name != null && name.length() > 0) {
				rLabelService.save(recipeLabel);
			}
		} catch (Exception e) {
			log.error("We have an exception in the createlabel method.");
			response= new ResponseEntity<RecipeLabel>(recipeLabel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
		
	}
	
	@PutMapping(path = "label/{labelId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeLabel> updateLabel(@RequestBody RecipeLabel recipeLabel, @PathVariable("labelId") Long id) {
		ResponseEntity<RecipeLabel> response= new ResponseEntity<RecipeLabel>(recipeLabel, HttpStatus.OK);
		
		try {
			String name = recipeLabel.getName();
			RecipeLabel label = rLabelService.findById(id);
			if(rLabelService.existsByName(name) == false && name != null && name.length() > 0) {
				label.setName(name);
				label.setSortOrder(recipeLabel.getSortOrder());
				rLabelService.save(label);
				response= new ResponseEntity<RecipeLabel>(label, HttpStatus.ACCEPTED);
			} else {
				//label name not valid or already in use
				log.warn("Unable to update recipelabel. Name not valid or already in us.");
				response= new ResponseEntity<RecipeLabel>(label, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("We have an exception in the updatelabel method.");
			response= new ResponseEntity<RecipeLabel>(recipeLabel, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
		
	}
	
	@DeleteMapping(path = "deletelabel/{labelId}")
	public ResponseEntity deleteLabel(@PathVariable("labelId") Long id) {
		ResponseEntity response= new ResponseEntity( HttpStatus.OK);
		
		try {
			//String name = recipeLabel.getName();
			rLabelService.deleteById(id);
		
		} catch (Exception e) {
			log.error("We have an exception in the deletelabel method.");
			response= new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
		
	}
}
