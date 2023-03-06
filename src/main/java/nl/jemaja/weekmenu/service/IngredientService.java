package nl.jemaja.weekmenu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.Ingredient;
import nl.jemaja.weekmenu.model.RecipeLabel;
import nl.jemaja.weekmenu.repository.IngredientRepository;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;

@Service
@Slf4j
public class IngredientService {

	@Autowired
	IngredientRepository ingredientRepo;

	public Ingredient findById(Long id) throws NotFoundException {
		Optional<Ingredient> ingredient = ingredientRepo.findById(id);
		try {
			return ingredient.get();
		} catch (NoSuchElementException e) {
			log.debug("NoSuchElementException " + e.getMessage());
			throw new NotFoundException("Could not find recipelabel with id: "+id);
		}
	}

	public boolean existsByName(String name) {
		List<Ingredient> ingredientList = new ArrayList<>();
		ingredientList = ingredientRepo.findByName(name);
		if(ingredientList.isEmpty()) return false;
		return true;
	}

	public void save(Ingredient ingredient) {
		ingredientRepo.save(ingredient);
		
	}

	public void deleteById(Long id) {
		ingredientRepo.deleteById(id);
		
	}

	
	
}
