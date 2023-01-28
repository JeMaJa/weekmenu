/**
 * 
 */
package nl.jemaja.weekmenu.dto.mapper;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.dto.DayRecipeDto;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeStatus;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yannick.tollenaere
 *
 *	private Date date;
	private int recipeId;
	private String recipeName;
	private Boolean workDay;
	private int status;
 */
@Slf4j
@Component
public class DayRecipeMapper {

	@Autowired
	RecipeService rService; 
	public DayRecipe dayRecipeDtoToDayRecipe(DayRecipeDto dto) {
		DayRecipe dr = new DayRecipe();
		if(dto.getRecipeId() != null) { 
			try {
				Recipe rec = rService.findByRecipeId(dto.getRecipeId());
				dr = DayRecipe.builder()
						.date(dto.getDate())
						.recipe(rec)
						.status(dto.getStatus())
						.id(dto.getId())
						.build();
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				log.debug("no recipe");
				log.error(e.getMessage());
			} 
		}	else {
			dr = DayRecipe.builder()
					.date(dto.getDate())
					.status(dto.getStatus())
					.id(dto.getId())
					.build();
		}
		RecipeMapper map = new RecipeMapper();
		return dr;

	}
	public DayRecipeDto dayRecipeToDayRecipeDto(DayRecipe dr) {
		//RecipeMapper map = new RecipeMapper();
		DayRecipeDto dto = new DayRecipeDto();
		if(dr.getRecipe() != null) {
			dto = DayRecipeDto.builder()
					.date(dr.getDate())
					//.recipeDto(map.recipeToRecipeDto(dr.getRecipe()))
					.recipeName(dr.getRecipe().getRecipeName())
					.recipeId(dr.getRecipe().getRecipeId())
					.status(dr.getStatus())
					.id(dr.getId())
					.build();
		} else {
			dto = DayRecipeDto.builder()
					.date(dr.getDate())
					.status(dr.getStatus())
					.id(dr.getId())
					.build();
		}

		return dto;
	}

}
