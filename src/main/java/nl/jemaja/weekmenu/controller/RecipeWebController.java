/**
 * 
 */
package nl.jemaja.weekmenu.controller;

//import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import nl.jemaja.weekmenu.dto.RecipeDto;
import nl.jemaja.weekmenu.dto.mapper.RecipeMapper;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeList;
import nl.jemaja.weekmenu.repository.RecipeRepository;
import nl.jemaja.weekmenu.service.RecipeService;

/**
 * @author yannick.tollenaere
 *
 */
@Controller
public class RecipeWebController {
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private RecipeRepository recipeRepository;
	
//	private RecipeMapper mapper = Mappers.getMapper(RecipeMapper.class);
	  @GetMapping("/test")	 
	public String getRecipe(ModelMap model) {
		Recipe rec = Recipe.builder()
					.recipeName("Goulash")
					.build();
		
		try {
			recipeService.save(rec);
		} catch (Exception e) {
			System.out.println("Boeoeoeoeoe");
		}
		Recipe rec2 = Recipe.builder()
				.recipeName("Soep")
				.build();
	
		try {
			recipeService.save(rec2);
		} catch (Exception e) {
			System.out.println("Boeoeoeoeoe");
		}
		RecipeList recipeList = new RecipeList();
		//to do, make a query to get all the recipes from the DB and put them in recipeList
		model.put("recipeList", recipeList);
		
		
		
		return "recipe";
		
	}
	  @GetMapping("/newrecipe")
	  String newRecipe(ModelMap model) {
		  RecipeDto recipeDto = new RecipeDto();
		  model.put("recipeDto", recipeDto);
		  return "newrecipe";
	  }
	
	  @PostMapping("/newrecipe")
		public String postRecipe(RecipeDto recipeDto) {
		  
		  System.out.println("dto: "+recipeDto);
		  RecipeMapper mapper = new RecipeMapper();
		    Recipe recipe = mapper.recipeDtoToRecipe(recipeDto);
			recipe = recipeService.save(recipe);
			
			System.out.println(recipe);
			
			return "/blaat";
			
		}
	


}
