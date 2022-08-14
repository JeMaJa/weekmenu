package nl.jemaja.weekmenu.dto.mapper;




import org.springframework.stereotype.Component;

import nl.jemaja.weekmenu.dto.RecipeDto;
import nl.jemaja.weekmenu.model.Recipe;

@Component
public class RecipeMapper {
	public Recipe recipeDtoToRecipe(RecipeDto recipeDto) {
		return Recipe.builder()
				.recipeName(recipeDto.getRecipeName())
				.recipeId(recipeDto.getRecipeId())
				.externalUrl(recipeDto.getExternalUrl())
				.description(recipeDto.getDescription())
				.vega(recipeDto.isVega())
				.workdayOk(recipeDto.isWorkdayOk())
				.healthScore(recipeDto.getHealthScore())
				.complexity(recipeDto.getComplexity())
				.coolDown(recipeDto.isCooldDown())
				.build();
	}
	
	public RecipeDto recipeToRecipeDto(Recipe recipe) {
		return RecipeDto.builder()
				.recipeName(recipe.getRecipeName())
				.recipeId(recipe.getRecipeId())
				.externalUrl(recipe.getExternalUrl())
				.description(recipe.getDescription())
				.vega(recipe.isVega())
				.workdayOk(recipe.isWorkdayOk())
				.healthScore(recipe.getHealthScore())
				.complexity(recipe.getComplexity())
				.cooldDown(recipe.isCoolDown())
				.build();
	}

}
