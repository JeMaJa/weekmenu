package nl.jemaja.weekmenu.service;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.RecipeLabel;
import nl.jemaja.weekmenu.repository.RecipeLabelRepository;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RecipeLabelService {
	
	@Autowired 
	RecipeLabelRepository recipeLabelRepo;
	

	public RecipeLabel findById(long id) throws NotFoundException {
		RecipeLabel label = recipeLabelRepo.findById(id);
		if(label == null) throw new NotFoundException("Could not find recipelabel with id: "+id);
		return label;
		
	}
	
	public List<RecipeLabel> findByRecipe(long recipeId) throws NotFoundException {
		List<RecipeLabel> labelList = new ArrayList<>();
		//Recipe recipe = rService.findByRecipeId(recipeId);
		labelList = recipeLabelRepo.findByRecipe(recipeId);
		if(labelList.isEmpty()) throw new NotFoundException("Could not find recipelabel with recipe: "+recipeId);
		return labelList;
		
	}

	public void save(RecipeLabel label) {
		recipeLabelRepo.save(label);
				
	}

	public boolean existsByName(String name) {
		List<RecipeLabel> labelList = new ArrayList<>();
		labelList = recipeLabelRepo.findByName(name);
		if(labelList.isEmpty()) return false;
		return true;
	}

	public void deleteById(Long id) {
		recipeLabelRepo.deleteById(id);
		log.debug("deleted: "+id);
		
	}
	

}
