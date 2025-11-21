package nl.jemaja.weekmenu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.Ingredient;
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
			throw new NotFoundException("Could not find ingredient with id: "+id);
		}
	}

	/**
	 * Check if an ingredient exists by name (exact match)
	 * @deprecated Use existsByNameIgnoreCase for better duplicate detection
	 */
	public boolean existsByName(String name) {
		List<Ingredient> ingredientList = new ArrayList<>();
		ingredientList = ingredientRepo.findByName(name);
		if(ingredientList.isEmpty()) return false;
		return true;
	}
	
	/**
	 * Check if an ingredient exists by name (case-insensitive)
	 * This prevents duplicates like "Flour" and "flour"
	 * 
	 * @param name The ingredient name to check
	 * @return true if ingredient exists (case-insensitive), false otherwise
	 */
	public boolean existsByNameIgnoreCase(String name) {
		List<Ingredient> ingredientList = ingredientRepo.findByNameIgnoreCase(name);
		return !ingredientList.isEmpty();
	}
	
	/**
	 * Find ingredient by name (case-insensitive)
	 * 
	 * @param name The ingredient name to search for
	 * @return List of ingredients matching the name (case-insensitive)
	 */
	public List<Ingredient> findByNameIgnoreCase(String name) {
		return ingredientRepo.findByNameIgnoreCase(name);
	}

	public void save(Ingredient ingredient) {
		ingredientRepo.save(ingredient);
	}

	public void deleteById(Long id) {
		ingredientRepo.deleteById(id);
	}

	public List<Ingredient> findAll() {
		return ingredientRepo.findAll();
	}

	public List<Ingredient> findAllOrderByNameAsc() {
		return ingredientRepo.findAllByOrderByNameAsc();
	}
}
