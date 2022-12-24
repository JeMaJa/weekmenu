package nl.jemaja.weekmenu.domain;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeLabel;
import nl.jemaja.weekmenu.repository.RecipeLabelRepository;
import nl.jemaja.weekmenu.repository.RecipeRepository;
import nl.jemaja.weekmenu.service.RecipeLabelService;
import nl.jemaja.weekmenu.service.RecipeService;

@Slf4j
@Configurable
@SpringBootTest
public class RecipeLabelTest {
	@Autowired
	RecipeLabelService labelService;
	
	@Autowired
	RecipeLabelRepository labelRepo;
	
	@Autowired
	RecipeService recipeService;
	
	@Autowired
	RecipeRepository recipeRepo;
	
	@Test
	void createLabelTest() {
		RecipeLabel label1 = RecipeLabel.builder().name("name1").sortOrder(10).build();
		RecipeLabel label2 = RecipeLabel.builder().name("name2").sortOrder(20).build();
		List<RecipeLabel> list = new ArrayList();
		list.add(label2);
		list.add(label1);
		Recipe recipe = Recipe.builder().recipeName("r1").servings(2).labels(list).build();
		System.out.println(recipe.getLabels().toString());
		
		
		
	}
	@Test
	void findLabelTest() {
		RecipeLabel label3 = RecipeLabel.builder().name("name3").sortOrder(10).build();
		RecipeLabel label4 = RecipeLabel.builder().name("name3").sortOrder(20).build();
		labelService.save(label3);
		labelService.save(label4);
		List<RecipeLabel> list = new ArrayList();
		list.add(label3);
		list.add(label4);
		Recipe recipe = Recipe.builder().recipeName("r2").servings(2).labels(list).build();
		recipeService.save(recipe);
		
	}

}
