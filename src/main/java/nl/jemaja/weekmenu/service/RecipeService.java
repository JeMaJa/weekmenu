/**
 * 
 */
package nl.jemaja.weekmenu.service;

import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.DayRecipePagedList;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipePagedList;
import nl.jemaja.weekmenu.repository.RecipeRepository;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;

/**
 * @author yannick.tollenaere
 * Service to get recipes
 */
@Slf4j
@Service
public class RecipeService {


	@Autowired
	RecipeRepository recipeRepo;

	/*
	 * Method: save(Recipe recipe) 
	 * Purpose: save a new Recipe to the database
	 * Will only save new records and not update existing.
	 * In case the save was not succesfull because the record already exists, this is retuned in the status
	 * 
	 */

	public TreeMap<String,Object> save(Recipe recipe) {
		TreeMap<String, Object> result = new TreeMap();
		try {
			recipeRepo.save(recipe);
			result.put("Status", "saved");
			result.put("Recipe", recipe);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			log.error("Unique constraint violation.1");
			log.debug(e.getMessage());
			result.put("Status", "error");
			result.put("Exception", e);
			//return recipeRepo.findByRecipeName(recipe.getRecipeName()).get(0);

		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			log.error("Unique constraint violation.2");
			log.debug(e.getMessage());
			result.put("Status", "duplicate");
			result.put("Exception", e);

		}

		return result;
	}

	public TreeMap<String, Object> saveOrUpdate(Recipe recipe) {
		TreeMap<String, Object> result = new TreeMap();
		try {
			recipeRepo.save(recipe);
			result.put("Status", "saved");
			result.put("Recipe", recipe);
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			log.error("Unique constraint violation.1");
			log.debug(e.getMessage());
			result.put("Status", "error");
			//return recipeRepo.findByRecipeName(recipe.getRecipeName()).get(0);

		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			log.error("Unique constraint violation.2");
			log.debug(e.getMessage());
			//lets update
			try {
				//update(String recipeName, String externalUrl, String description, int complexity, boolean workdayOk, int healthScore, boolean vega, Long recipeId);
				Recipe recipeOld = recipeRepo.findByRecipeName(recipe.getRecipeName()).get(0);
				log.debug("found ID to update: "+recipeOld.getRecipeId());
				recipeRepo.update(recipe.getRecipeName(),recipe.getExternalUrl(),recipe.getDescription(),recipe.getComplexity(),recipe.isWorkdayOk(),recipe.getHealthScore(),recipe.isVega(),recipeOld.getRecipeId());
				recipe = recipeRepo.findByRecipeName(recipe.getRecipeName()).get(0);
				result.put("Status", "updated");
				result.put("Recipe", recipe);
			} catch (Exception e2) {
				//Update error
				log.error("Update error for recipe: "+recipe.getRecipeId());
				log.debug(e2.getMessage());
				result.put("Status", "error");
			}
		}


		return result;

	}
	public Recipe findByRecipeName(String name) throws NotFoundException {
		Recipe recipe = recipeRepo.findByRecipeName(name).get(0);
		if(recipe == null) throw new NotFoundException("Could not find recipe with name: "+name);
		return recipe;
	}

	public Recipe findByRecipeId(long id) throws NotFoundException{
		Recipe recipe = recipeRepo.findByRecipeId(id);
		if(recipe == null) throw new NotFoundException("Could not find recipe with id: "+id);
		return recipe;
	}

	public RecipePagedList listRecipe(PageRequest pageRequest) {

		RecipePagedList recipePagedList = null;
		Page<Recipe> recipePage = null;


		log.debug("In listRecipe");
		log.debug("looking for all recipes");
		recipePage = recipeRepo.findAll(pageRequest);

		log.debug("Making the page list??");
		recipePagedList = new RecipePagedList(recipePage
				.getContent()
				.stream()
				.collect(Collectors.toList()),
				PageRequest
				.of(recipePage.getPageable().getPageNumber(),
						recipePage.getPageable().getPageSize()),
				recipePage.getTotalElements());
		return recipePagedList;
	}

	public RecipePagedList findLastEatenPage(Pageable sortedLastEatenAsc) {
		RecipePagedList recipePagedList = null;
		Page<Recipe> recipePage = null;

		recipePage = recipeRepo.findAll(sortedLastEatenAsc);
		log.debug("Making the page list??");
		recipePagedList = new RecipePagedList(recipePage
				.getContent()
				.stream()
				.collect(Collectors.toList()),
				PageRequest
				.of(recipePage.getPageable().getPageNumber(),
						recipePage.getPageable().getPageSize()),
				recipePage.getTotalElements());
		return recipePagedList;
	}


	public List<Recipe> findLastEaten() {
		return recipeRepo.findLastEaten();
	}

	public void setLastEaten(long recipeId, Date date) {
		// get last eaten, see if it is smaller
		Recipe recipe = recipeRepo.findByRecipeId(recipeId);
		if(recipe.getLastEaten() == null | recipe.getLastEaten().before(date)) {
			log.debug("New date is > old date, so we update.");
			recipeRepo.updateLastEaten(recipeId, date);
		}
		
	}



}
