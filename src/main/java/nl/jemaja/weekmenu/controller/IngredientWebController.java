package nl.jemaja.weekmenu.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.dto.InfoDto;
import nl.jemaja.weekmenu.dto.RecipeDto;
import nl.jemaja.weekmenu.model.Ingredient;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.service.IngredientService;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;

@Slf4j
@Controller
public class IngredientWebController {

	@Autowired
	IngredientService iService;

	@GetMapping("/ingredients")
	String getIngredient (ModelMap map) {
		List<Ingredient> ingredients = iService.findAllOrderByNameAsc();
		map.addAttribute("ingredients", ingredients);
		return "getingredients";
	}
	
	@GetMapping("/ingredient")
	String getIngredient (Long id, ModelMap map) {
		return "ingredient"; 
	}

	@PostMapping("/ingredient/submit")
	public RedirectView submitPost(
			HttpServletRequest request, 
			@ModelAttribute Ingredient ingredient, 
			RedirectAttributes redirectAttributes) {
		InfoDto infoDto = new InfoDto();
		infoDto.setType("Info");
		infoDto.setBody("Saved ingredient");
		log.debug("PostMapping received ingredient "+ingredient);
		if(!ingredient.isUpdate()) {



			try {
				String name = ingredient.getName();
				if(iService.existsByName(name) == false && name != null && name.length() > 0) {
					iService.save(ingredient);
					
				}
			} catch (Exception e) {
				log.error("We have an exception in the createingredient method.");
				infoDto.setType("Error");
				infoDto.setBody("Error: "+e.getMessage());
			}
		} else {
			//Updating
			
			try {

				Ingredient ing = iService.findById(ingredient.getId());
				if(ingredient.validate()) {
					ing.update(ingredient);
					iService.save(ing);
					
				} else {
					//label name not valid or already in use
					log.warn("Unable to update ingredient. Name not valid or already in us.");
					infoDto.setType("Warning");
					infoDto.setBody("Unable to update ingredient. Name not valid or already in us.");

				}
			} catch (Exception e) {
				log.error("We have an exception in the updatelabel method.");
				infoDto.setType("Error");
				infoDto.setBody("Error: "+e.getMessage());
			
			}
			



		}
		return new RedirectView("/ingredients", true);
	}

	@GetMapping("/ingredient/submit")
	public String submitGet(Model model, HttpServletRequest request) {


		Ingredient ingredient = new Ingredient();
		InfoDto infoDto = new InfoDto();
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			log.debug("Got an inputFlashMap");
			ingredient = (Ingredient) inputFlashMap.get("ingredient");
			infoDto = (InfoDto) inputFlashMap.get("infoDto");
		}


		model.addAttribute("ingredient", ingredient);
		model.addAttribute("infoDto", infoDto);

		return "newingredient";
	}
	@GetMapping("modifyingredient")
	public RedirectView modifyIngredient(Long id,  RedirectAttributes redirectAttributes) {
		/*
		 * Will redirect to the create recipe page with a flash attribute
		 * 
		 */

		InfoDto infoDto = new InfoDto();
		Ingredient ingredient = new Ingredient();
		try {
			 ingredient = iService.findById(id);
			infoDto.setType("Info");
			infoDto.setBody("Modify this ingredient.");
			
			ingredient.setUpdate(true);
		} catch (NotFoundException e) {
			infoDto.setType("Warning");
			infoDto.setBody("No Ingredient found with this ID.");
		}

		log.debug("Preparing to modify ingredient: "+ingredient);


		redirectAttributes.addFlashAttribute("infoDto", infoDto);
		redirectAttributes.addFlashAttribute("ingredient", ingredient);
		return new RedirectView("/ingredient/submit", true);
	}

}
