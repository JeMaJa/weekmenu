/*package nl.jemaja.weekmenu.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.RequiredArgsConstructor;
import nl.jemaja.weekmenu.dto.RecipeDto;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.service.RecipeService;

@RequiredArgsConstructor
public abstract class RecipeMapperDecorator implements RecipeMapper {
	private RecipeService recipeService;
    private RecipeMapper mapper;
	@Autowired
    public void setRecipeService(RecipeService recipeService) {
		this.recipeService = recipeService;
	}
	@Autowired
	public void setMapper(RecipeMapper mapper) {
		this.mapper = mapper;
	}
	@Override
	public Recipe recipeDtoToRecipe(RecipeDto recipeDto) {
		
		return mapper.recipeDtoToRecipe(recipeDto);
	}
	@Override
	public RecipeDto recipeToRecipeDto(Recipe recipe) {
		
		return mapper.recipeToRecipeDto(recipe);
	}
	
	

    
}*/
