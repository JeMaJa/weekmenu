/**
 * 
 */
package nl.jemaja.weekmenu.controller;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.dto.InfoDto;
import nl.jemaja.weekmenu.dto.RecipeDto;
import nl.jemaja.weekmenu.dto.RecipeStatsDto;
import nl.jemaja.weekmenu.dto.mapper.RecipeMapper;
import nl.jemaja.weekmenu.model.LabelColor;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeLabel;
import nl.jemaja.weekmenu.repository.RecipeRepository;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.RecipeLabelService;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author yannick.tollenaere
 *
 */
@Slf4j
@Controller
public class RecipeWebController {


	@Autowired
	private RecipeService recipeService;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	DayRecipeService dRService;

	@Autowired
	RecipeLabelService lService;

	@Autowired
	private RecipeMapper mapper;

	@GetMapping(path = "/getrecipes") 
	public String getRecipes(ModelMap map,@RequestParam("page") Optional<Integer> page, 
			@RequestParam("size") Optional<Integer> size, @RequestParam("q") Optional<String> query, Long labelId) {
		int currentPage = page.orElse(0);
		int pageSize = size.orElse(10);
		Page<Recipe> recipePage = null;
		PageRequest sortedByName = PageRequest.of(currentPage, pageSize, Sort.by("recipeName").ascending());
		if(!query.isEmpty()) {
			String q = "%"+query.get().toLowerCase()+"%";
			recipePage = recipeService.findByRecipeNameOrDescriptionContaining(q,q, sortedByName);
		} else if(labelId != null) {
			/* get by a label */
			RecipeLabel label = null;
			try {
				label = lService.findById(labelId);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				log.error("no recipes with label found");
			}

			recipePage = recipeService.findByLabel(label,sortedByName);
		} else {
			recipePage = recipeService.findAll(sortedByName);
		}

		map.addAttribute("recipePage", recipePage);

		int totalPages = recipePage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			map.addAttribute("pageNumbers", pageNumbers);
		}
		return "getrecipes";
	}


	@GetMapping("/recipe")
	String recipe(ModelMap model, Long recipeId) {

		if(recipeId != null){
			Recipe recipe = new Recipe();
			RecipeStatsDto stats = new RecipeStatsDto();
			List<RecipeLabel> labels = new ArrayList<RecipeLabel>();
			try {
				log.debug("retrieving single recipe: "+recipeId);
				recipe = recipeService.findByRecipeId(recipeId);
				stats.setLastEaten(recipeService.findLastEaten(recipe));
				stats.setNextEaten(recipeService.findNextEaten(recipe));
				labels = recipeService.getLabels(recipe);
				RecipeDto recipeDto = mapper.recipeToRecipeDto(recipe);
				recipeDto.setDescription(recipeDto.getDescription().replaceAll("(\r\n|\n)", "<br>"));
				if(recipeDto.getShortDescription() != null)
				{
					recipeDto.setShortDescription(recipeDto.getShortDescription().replaceAll("(\r\n|\n)", "<br>"));
				}
				for(RecipeLabel label : labels) {
					if(label.getColor() == null) {
						label.setColor(LabelColor.GREY);

					}
				}
				model.put("stats", stats);
				model.put("recipeDto", recipeDto);
				model.put("labels", labels);
				return "recipe";

			} catch (NotFoundException e) {
				log.error("recipe not found/");
				e.printStackTrace();
				return "redirect:/getrecipes";
			}


		} else {
			/*
			 * No recipe ID provided,show all 
			 */
			log.debug("Showing all recipes");
			return "redirect:/getrecipes";
		}



	}
	@GetMapping("modifyrecipe")
	public RedirectView modifyRecipe(Long recipeId,  RedirectAttributes redirectAttributes) {
		/*
		 * Will redirect to the create recipe page with a flash attribute
		 * 
		 */

		InfoDto infoDto = new InfoDto();
		RecipeDto recipeDto = new RecipeDto();
		try {
			Recipe recipe = recipeService.findByRecipeId(recipeId);
			infoDto.setType("Info");
			infoDto.setBody("Modify this recipe.");
			recipeDto = mapper.recipeToRecipeDto(recipe);
			recipeDto.setUpdate(true);
		} catch (NotFoundException e) {
			infoDto.setType("Warning");
			infoDto.setBody("No recipe found with this ID, feel free to create a new recipe.");
		}

		log.debug("Preparing to modify recipe: "+recipeId);


		redirectAttributes.addFlashAttribute("infoDto", infoDto);
		redirectAttributes.addFlashAttribute("recipeDto", recipeDto);
		return new RedirectView("/recipe/submit", true);
	}
	/*
	 * Try via https://www.baeldung.com/spring-web-flash-attributes
	 * 
	 */
	@GetMapping("/recipe/submit")
	public String submitGet(Model model, HttpServletRequest request) {

		RecipeDto recipeDto = new RecipeDto();
		recipeDto.setActive(true);
		InfoDto infoDto = new InfoDto();
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			log.debug("Got an inputFlashMap");
			recipeDto = (RecipeDto) inputFlashMap.get("recipeDto");
			infoDto = (InfoDto) inputFlashMap.get("infoDto");
		}


		model.addAttribute("recipeDto", recipeDto);
		model.addAttribute("infoDto", infoDto);

		return "newrecipe";
	}
	@PostMapping("/recipe/submit")
	public RedirectView submitPost(
			HttpServletRequest request, 
			@ModelAttribute RecipeDto recipeDto, 
			RedirectAttributes redirectAttributes) {
		
		RecipeMapper mapper = new RecipeMapper();
		log.debug("PostMapping received Dto "+recipeDto);
		if(!recipeDto.isUpdate()) {
			
			Recipe recipe = mapper.recipeDtoToRecipe(recipeDto);
			InfoDto infoDto = new InfoDto();
			TreeMap<String, Object> result = recipeService.save(recipe);
			if(result.get("Recipe") != null && result.get("Status").toString().equals("saved")) {
				recipe = (Recipe) result.get("Recipe"); 
				//we now have the saved Dto, with the right ID
				infoDto.setType("Success");
				infoDto.setBody("Saved recipe: "+recipe.getRecipeName());
				redirectAttributes.addFlashAttribute("recipeDto", recipeDto);
				redirectAttributes.addFlashAttribute("infoDto", infoDto);
				return new RedirectView("/recipe/success", true);
			} else if(result.get("Status").toString().equals("duplicate")) {
				log.debug("in duplicate");
				infoDto.setType("Warning");
				infoDto.setBody("A recipe with this name already exists.");
				redirectAttributes.addFlashAttribute("infoDto", infoDto);
				redirectAttributes.addFlashAttribute("recipeDto", recipeDto);
				return new RedirectView("/recipe/submit", true);
			} else {
				log.error("in error");
				infoDto.setType("Error");
				infoDto.setBody("An Error occured.");
				redirectAttributes.addFlashAttribute("infoDto", infoDto);
				redirectAttributes.addFlashAttribute("recipeDto", recipeDto);
				return new RedirectView("/recipe/submit", true);
			}
		} else {
			//Updating
			Recipe existingRecipe;
			try {
				existingRecipe = recipeService.findByRecipeId(recipeDto.getRecipeId());
				existingRecipe.setComplexity(recipeDto.getComplexity());
				existingRecipe.setDescription(recipeDto.getDescription());
				existingRecipe.setVega(recipeDto.isVega());
				existingRecipe.setShortDescription(recipeDto.getShortDescription());
				existingRecipe.setWorkdayOk(recipeDto.isWorkdayOk());	
				existingRecipe.setHealthScore(recipeDto.getHealthScore());
				existingRecipe.setImageUrl(recipeDto.getImageUrl());
				existingRecipe.setServings(recipeDto.getServings());
				existingRecipe.setExternalUrl(recipeDto.getExternalUrl());
				existingRecipe.setRecipeName(recipeDto.getRecipeName());
				existingRecipe.setActive(recipeDto.isActive());
				recipeService.saveOrUpdate(existingRecipe);
				
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new RedirectView("/recipe?recipeId="+recipeDto.getRecipeId(), true);
			
			

		}

	}

	@GetMapping("/recipe/success")
	public String getSuccess(HttpServletRequest request) {
		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null) {
			RecipeDto recipeDto = (RecipeDto) inputFlashMap.get("recipeDto");
			InfoDto infoDto = (InfoDto) inputFlashMap.get("infoDto");
			log.debug(infoDto.toString());
			log.debug(recipeDto.toString());
			return "success";
		} else {
			return "redirect:/recipe/submit";
		}
	}


}
