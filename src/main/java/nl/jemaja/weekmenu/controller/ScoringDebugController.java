package nl.jemaja.weekmenu.controller;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.Settings;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.service.RecipeScoringService;
import nl.jemaja.weekmenu.service.SettingsService;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.model.DayRecipe;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Debug controller for visualizing RecipeScoringService calculations
 * Useful for understanding and tuning the scoring algorithm
 */
@Slf4j
@Controller
public class ScoringDebugController {

    private final RecipeScoringService recipeScoringService;
    private final RecipeService recipeService;
    private final SettingsService settingsService;
    private final DayRecipeService dayRecipeService;

    public ScoringDebugController(RecipeScoringService recipeScoringService, 
                                   RecipeService recipeService,
                                   SettingsService settingsService,
                                   DayRecipeService dayRecipeService) {
        this.recipeScoringService = recipeScoringService;
        this.recipeService = recipeService;
        this.settingsService = settingsService;
        this.dayRecipeService = dayRecipeService;
    }

    @GetMapping("/debug/scoring")
    public String showScoringDebug(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            ModelMap map) {
        
        // Default to today if no date provided
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        Date sqlDate = Date.valueOf(targetDate);
        
        log.debug("Loading scoring debug page for date: {}", targetDate);
        
        // Get all recipes
        List<Recipe> recipes = recipeService.findAll();
        
        // Get week context for variety score
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(sqlDate);
        calStart.add(Calendar.DATE, -6);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(sqlDate);
        calEnd.add(Calendar.DATE, 6);
        Date startDate = new Date(calStart.getTimeInMillis());
        Date endDate = new Date(calEnd.getTimeInMillis());
        List<DayRecipe> weekContext = dayRecipeService.findByDateBetween(startDate, endDate);
        
        // Build score details for each recipe
        List<RecipeScoreDetail> scoreDetails = new ArrayList<>();
        for (Recipe recipe : recipes) {
            RecipeScoreDetail detail = new RecipeScoreDetail();
            detail.setRecipe(recipe);
            detail.setHealthScore(recipeScoringService.calcHealthScore(recipe));
            detail.setPreferenceScore(recipeScoringService.calcPreferenceScore(recipe, sqlDate));
            detail.setRecencyScore(recipeScoringService.calcRecencyScore(recipe, sqlDate));
            detail.setVarietyScore(recipeScoringService.calcVarietyScore(recipe, sqlDate, weekContext));
            detail.setTotalScore(recipeScoringService.calculateScore(recipe, sqlDate));
            scoreDetails.add(detail);
        }
        
        // Sort by total score descending
        scoreDetails.sort((a, b) -> Double.compare(b.getTotalScore(), a.getTotalScore()));
        
        // Get current settings for display
        Settings settings = settingsService.getSettings();
        
        map.addAttribute("scoreDetails", scoreDetails);
        map.addAttribute("targetDate", targetDate);
        map.addAttribute("settings", settings);
        map.addAttribute("weekContext", weekContext);
        
        return "scoringdebug";
    }
    
    /**
     * DTO to hold score breakdown for a recipe
     */
    public static class RecipeScoreDetail {
        private Recipe recipe;
        private double healthScore;
        private double preferenceScore;
        private double recencyScore;
        private double varietyScore;
        private double totalScore;
        
        // Getters and setters
        public Recipe getRecipe() { return recipe; }
        public void setRecipe(Recipe recipe) { this.recipe = recipe; }
        
        public double getHealthScore() { return healthScore; }
        public void setHealthScore(double healthScore) { this.healthScore = healthScore; }
        
        public double getPreferenceScore() { return preferenceScore; }
        public void setPreferenceScore(double preferenceScore) { this.preferenceScore = preferenceScore; }
        
        public double getRecencyScore() { return recencyScore; }
        public void setRecencyScore(double recencyScore) { this.recencyScore = recencyScore; }
        
        public double getVarietyScore() { return varietyScore; }
        public void setVarietyScore(double varietyScore) { this.varietyScore = varietyScore; }
        
        public double getTotalScore() { return totalScore; }
        public void setTotalScore(double totalScore) { this.totalScore = totalScore; }
    }
}
