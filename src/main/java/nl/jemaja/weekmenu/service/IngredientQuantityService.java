package nl.jemaja.weekmenu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.Ingredient;
import nl.jemaja.weekmenu.model.IngredientQuantity;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.repository.IngredientQuantityRepository;

@Service
@Slf4j
public class IngredientQuantityService {

	@Autowired
	IngredientQuantityRepository iQRepository;

	public IngredientQuantity findByRecipeAndIngredient(Recipe recipe, Ingredient ingredient) {
		// TODO Auto-generated method stub
		return iQRepository.findByRecipeAndIngredient(recipe, ingredient);
	}



	public void save(IngredientQuantity iQuantity) {
		iQRepository.save(iQuantity);
		
	}



	public List<IngredientQuantity> findByRecipe(Recipe recipe) {
	
		return iQRepository.findByRecipe(recipe);
	}


}
