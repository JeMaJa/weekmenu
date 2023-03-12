/**
 * 
 */
package nl.jemaja.weekmenu.service;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.dto.RecipeStatsDto;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeLabel;
import nl.jemaja.weekmenu.model.RecipePagedList;
import nl.jemaja.weekmenu.repository.DayRecipeRepository;
import nl.jemaja.weekmenu.repository.RecipeRepository;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author yannick.tollenaere
 * Service to get recipes
 */
@Slf4j
@Service
public class RecipeService {


	@Autowired
	RecipeRepository recipeRepo;
	
	@Autowired
	DayRecipeService dRService;
	
	@Autowired
	DayRecipeRepository dRRepo;

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
			recipe.setActive(true); // issue#24 by default set active to true
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


	public Date findLastEaten(Recipe recipe) {
		Date currentdate = new java.sql.Date(System.currentTimeMillis());
		return this.findLastEaten(recipe,currentdate);
	}
	
	public Date findLastEaten(Recipe recipe, Date date) {
		Date returnVal = dRRepo.findPrevLastEaten(recipe,date);
		if(returnVal == null) {
			return new Date(0);
		} else {
			return returnVal;
		}
	}
	
	
	

	public List<Recipe> findAll() {
				return recipeRepo.findAll();
	}
	
	public Recipe setLabel(Recipe recipe, RecipeLabel label) {
		List<RecipeLabel> list = (List<RecipeLabel>) recipe.getLabels();
	// check if label is already in
		if(!list.contains(label) || list.isEmpty()) {
			log.debug("Adding label");
			list.add(label);
			recipe.setLabels(list);
			this.save(recipe);
		}
		return recipe;
		
	}
	
	public List<RecipeLabel> getLabels(Recipe recipe){
		return recipe.getLabels();
		
	}
	
	public RecipeStatsDto getStats(Recipe recipe) {
		Date currentdate = new java.sql.Date(System.currentTimeMillis());
		Date lastEaten = dRRepo.findPrevLastEaten(recipe,currentdate); 
		Date nextEaten = dRRepo.findNextEaten(recipe,currentdate); 
		int timesEaten = dRRepo.countEaten(recipe,currentdate);
		long total = dRRepo.count();
		RecipeStatsDto dto = RecipeStatsDto.builder().timesEaten(timesEaten).lastEaten(lastEaten).nextEaten(nextEaten).percentageEaten((timesEaten/total)*100).build();
		
		return dto;
		
		
		
	}

	public List<Recipe> findLastEaten() {
		// TODO Auto-generated method stub
		List<Recipe> unSorted = this.findAll();
		List<Recipe> sorted = new LinkedList<Recipe>();
		TreeMap<Date, Recipe> map = new TreeMap<Date, Recipe>();
		for(int i =0;i<unSorted.size();i++)
		{
			map.put(this.findLastEaten(unSorted.get(i)), unSorted.get(i));
		}
		for(Date key : map.keySet()) {
		    sorted.add(map.get(key));
		}
		
		return sorted;
		
		
	}



	public List<Recipe> findAllActive() {
		return recipeRepo.findByActiveTrue();
	}

	public List<Recipe> findAllWorkdayActive() {
		return recipeRepo.findByActiveTrueAndWorkdayOkTrue();
	}

	public java.util.Date findNextEaten(Recipe recipe) {
		Date currentdate = new java.sql.Date(System.currentTimeMillis());
		return this.findNextEaten(recipe,currentdate);
	}

	private java.util.Date findNextEaten(Recipe recipe, Date currentdate) {
		Date returnVal = dRRepo.findNextEaten(recipe,currentdate);
		if(returnVal == null) {
			return new Date(0);
		} else {
			return returnVal;
		}
	}

	public Page<Recipe> findByRecipeNameOrDescriptionContaining(String q, String q2, PageRequest sortedByName) {
		
		return recipeRepo.findByRecipeNameOrDescriptionContaining(q, q2, sortedByName);
	}

	public Page<Recipe> findAll(PageRequest sortedByName) {
		
		return recipeRepo.findAll(sortedByName);
	}

	public Page<Recipe> findByLabel(RecipeLabel label, PageRequest sortedByName) {
		
		return recipeRepo.findByLabels(label, sortedByName);
	}


	





}
