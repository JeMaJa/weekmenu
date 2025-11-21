package nl.jemaja.weekmenu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller for the ingredient management page
 * Provides a centralized interface for viewing and managing all ingredients
 */
@Slf4j
@Controller
public class IngredientManagementController {

    @GetMapping("/ingredientmanagement")
    public String showIngredientManagement(ModelMap map) {
        log.debug("Loading ingredient management page");
        return "ingredientmanagement";
    }
}
