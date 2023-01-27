/**
 * 
 */
package nl.jemaja.weekmenu.controller;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.service.DayRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author yannick.tollenaere
 *
 */

@Slf4j
@Controller
public class DayRecipeWebController {
	
	@Autowired
	DayRecipeService dRService;
	
	@GetMapping("/dayrecipe") 
	String showDayRecipe(ModelMap model) {
		return "dayrecipe";
		
	}

}
