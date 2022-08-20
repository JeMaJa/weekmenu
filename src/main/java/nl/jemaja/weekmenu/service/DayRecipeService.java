package nl.jemaja.weekmenu.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;



import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.dto.DayRecipeDtoPagedList;
import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.DayRecipePagedList;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.repository.DayRecipeRepository;
@Slf4j
@Service
public class DayRecipeService {
	
	@Autowired
	DayRecipeRepository dayRecipeRepo;
	
	
	public DayRecipe save(DayRecipe dayRecipe) {
		dayRecipeRepo.save(dayRecipe);
		return dayRecipe;
		
	}
	public List<DayRecipe> findByDate(Date date) {
		return dayRecipeRepo.findByDate(date);
	}
	
	public List<DayRecipe> findByDateBetween(Date startDate, Date endDate) {
		return dayRecipeRepo.findByDateBetween(startDate, endDate);
	}
	
	public List<DayRecipe> findByrecipe(Recipe recipe) {
		return dayRecipeRepo.findByRecipe(recipe);
	}
	
	public Date findMaxDate() {
		return dayRecipeRepo.findMaxDate();
	}
	
	public int scheduleDiner(DayRecipe dayRecipe, Recipe recipe, RecipeService recipeService) {
		int returnVal = 0;
		log.debug("Scheduling dinner  with status 2 for: "+dayRecipe.getDate() + " to be: "+recipe.getRecipeName());
		try {
			returnVal = dinner(dayRecipe,recipe,recipeService,2);
		} catch (Exception e){
			returnVal = 1;
			log.debug("Exception in scheduleDiner: "+e.getMessage());
		}
		return returnVal;
	}
	
	public int suggestDiner(DayRecipe dayRecipe, Recipe recipe, RecipeService recipeService) {
		int returnVal = 0;
		log.debug("Scheduling dinner  with status 1 for: "+dayRecipe.getDate() + " to be: "+recipe.getRecipeName());
		try {
			returnVal = dinner(dayRecipe,recipe,recipeService,1);
		} catch (Exception e){
			returnVal = 1;
			log.debug("Exception in suggestDiner: "+e.getMessage());
		}
		return returnVal;
	}
	
	public int dinner(DayRecipe dayRecipe, Recipe recipe, RecipeService recipeService, int status) {
		int returnVal = 0;
		log.debug("Scheduling dinner"+dayRecipe.getDate() + " to be: "+recipe.getRecipeName());
		try {
			dayRecipeRepo.scheduleDinner(dayRecipe.getId(),recipe,status);
			recipeService.setLastEaten(recipe.getRecipeId(),dayRecipe.getDate()); 
		} catch (Exception e){
			returnVal = 1;
			log.debug("Exception in dinner: "+e.getMessage());
		}
		return returnVal;
	}
	
	

    public DayRecipePagedList listDayRecipe(Recipe recipe, Date startDate, Date endDate, PageRequest pageRequest) {

    	DayRecipePagedList dayRecipePagedList = null;
        Page<DayRecipe> dayRecipePage = null;

        
        log.debug("In listDayRecipe");
        if (!ObjectUtils.isEmpty(recipe) && startDate == null && endDate == null) {
            //search all days where we ate a recipe
        	log.debug("looking for: "+recipe.getRecipeName());
        	dayRecipePage = dayRecipeRepo.findByRecipe(recipe, pageRequest);
        } else if (ObjectUtils.isEmpty(recipe) && startDate != null) {
        	//search for a date range
        	if(endDate == null) {
        		endDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
        	}
        	log.debug("looking for dates: " + startDate + " " + endDate);
        	dayRecipePage = dayRecipeRepo.findByDateBetween(startDate, endDate, pageRequest);
        } else {
        	log.debug("looking for all day recipes");
        	dayRecipePage = dayRecipeRepo.findAll(pageRequest);
        }

       log.debug("Making the page list??");
        dayRecipePagedList = new DayRecipePagedList(dayRecipePage
                    .getContent()
                    .stream()
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(dayRecipePage.getPageable().getPageNumber(),
                            		dayRecipePage.getPageable().getPageSize()),
                            dayRecipePage.getTotalElements());
        return dayRecipePagedList;
        }
        
    

	
	public int creater(int num) {
		//Generates new DayRecipe items
		Date date = dayRecipeRepo.findMaxDate();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		Calendar end = Calendar.getInstance();
		end.setTime(date);
		end.add(Calendar.DATE, num);
		
		c.add(Calendar.DATE, 1);
		while(c.before(end)) {
			log.debug("Creating DayRecipe for: "+ c.toString());
			Boolean workDay = true;
			c.add(Calendar.DATE, 1);
			Date d = new Date(c.getTimeInMillis());
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
				    c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				workDay = false;
			}
			
			DayRecipe dR = DayRecipe.builder()
							.date(d)
							.recipe(null)
							.workday(workDay)
							.status(0)
							.build();
			dR = this.save(dR);
		}
		return 0;
	}
	public int creater(java.util.Date start, java.util.Date end) {
		//Generates new DayRecipe items
		
		Calendar c = Calendar.getInstance();
		c.setTime(start);
		
		Calendar endC = Calendar.getInstance();
		endC.setTime(end);
				
		c.add(Calendar.DATE, 1);
		while(c.before(endC)) {
			log.debug("Creating DayRecipe for: "+ c.toString());
			Boolean workDay = true;
			
			Date d = new Date(c.getTimeInMillis());
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
				    c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				workDay = false;
			}
			
			DayRecipe dR = DayRecipe.builder()
							.date(d)
							.recipe(null)
							.workday(workDay)
							.status(0)
							.build();
			try {
				save(dR);
			} catch (org.springframework.dao.DataIntegrityViolationException e) {
				//it already exists, that is fine just ignor
				log.debug("Duplicate exception, skipp to next");
			}
			c.add(Calendar.DATE, 1);
		}
		return 0;
	}

	public List<DayRecipe> getOrCreate(Date from, Date to) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(from);
		List<DayRecipe> returnList = new ArrayList<DayRecipe>();
		Calendar end = Calendar.getInstance();
		end.setTime(to);
		end.add(Calendar.DATE, 1);
		while(cal.before(end))
		{
			List<DayRecipe> dRL = findByDate(new Date(cal.getTimeInMillis()));
			if(dRL.isEmpty()) {
				Boolean workDay = true;
				
				log.debug("Create DayRecipe "+cal.getTime());
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
					    cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					workDay = false;
				}
				
				DayRecipe dr = DayRecipe.builder()
								.date(new Date(cal.getTimeInMillis()))
								.recipe(null)
								.workday(workDay)
								.status(0)
								.build();
				try {
					save(dr);
					returnList.add(dr);
				} catch (Exception e) {
					//Oh oh
					log.error(e.getMessage());
					log.debug(e.getStackTrace().toString());
				}
				
			} else {
				log.debug("found DayRecipe "+cal.getTime());
				returnList.add(dRL.get(0));
			}
			cal.add(Calendar.DATE, 1);
		}
		
		return returnList;
	}
	public DayRecipe findById(long dayRecipeId) {
		Optional<DayRecipe> opt = dayRecipeRepo.findById(dayRecipeId);
		return  opt.get();
	}


}
