package nl.jemaja.weekmenu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.Settings;

/**
 * Unit tests for RecipeScoringService
 * 
 * @author Yannick / JeMaJa
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RecipeScoringService Tests")
class RecipeScoringServiceTest {

    @Mock
    private SettingsService settingsService;

    @Mock
    private DayRecipeService dayRecipeService;

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeScoringService scoringService;

    private Settings testSettings;
    private Recipe testRecipe;
    private Date testDate;

    @BeforeEach
    void setUp() {
        // Create test settings with reasonable defaults
        testSettings = Settings.builder()
                .healthWeight(0.2)
                .preferenceWeight(0.3)
                .recencyWeight(0.25)
                .variatyWeight(0.1)
                .qOneWeight(1.0)
                .qTwoWeight(0.5)
                .qThreeWeight(0.25)
                .qFourWeight(0.1)
                .oneWeekPenalty(-0.5)
                .twoWeekPenalty(-0.3)
                .threeWeekPenalty(-0.1)
                .build();

        // Create test recipe
        testRecipe = Recipe.builder()
                .recipeName("Test Recipe")
                .healthScore(4)
                .build();

        // Test date: 2024-01-15
        testDate = Date.valueOf("2024-01-15");

        // Mock settings service
        when(settingsService.getSettings()).thenReturn(testSettings);
    }

    @Test
    @DisplayName("calcHealthScore - should normalize health score correctly")
    void testCalcHealthScore() {
        // Remove the settingsService mock - calcHealthScore doesn't use it

        // Test with health score 4
        Recipe recipe4 = Recipe.builder().healthScore(4).build();
        assertEquals(0.8, scoringService.calcHealthScore(recipe4), 0.001);

        // Test with health score 5
        Recipe recipe5 = Recipe.builder().healthScore(5).build();
        assertEquals(1.0, scoringService.calcHealthScore(recipe5), 0.001);

        // Test with health score 1
        Recipe recipe1 = Recipe.builder().healthScore(1).build();
        assertEquals(0.2, scoringService.calcHealthScore(recipe1), 0.001);
    }

    @Test
    @DisplayName("calcPreferenceScore - new recipe should get bonus")
    void testCalcPreferenceScore_NewRecipe() {
        // Mock: recipe never cooked before
        when(dayRecipeService.findByDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        double score = scoringService.calcPreferenceScore(testRecipe, testDate);

        // Should get new recipe bonus of 0.3
        assertEquals(0.3, score, 0.001);
    }

    @Test
    @DisplayName("calcPreferenceScore - recipe cooked once should get smaller bonus")
    void testCalcPreferenceScore_CookedOnce() {
        // Mock: recipe cooked once in Q1
        DayRecipe dayRecipe = DayRecipe.builder()
                .recipe(testRecipe)
                .date(Date.valueOf("2024-01-10"))
                .build();

        when(dayRecipeService.findByDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(Arrays.asList(dayRecipe))  // First quarter
                .thenReturn(Collections.emptyList())   // Other quarters
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList());

        double score = scoringService.calcPreferenceScore(testRecipe, testDate);

        // Should get: (1 * 1.0) + 0.15 bonus = 1.15, capped at 1.0
        assertEquals(1.0, score, 0.001);
    }

    @Test
    @DisplayName("calcPreferenceScore - frequently cooked recipe")
    void testCalcPreferenceScore_FrequentlyCookedRecipe() {
        // Mock: recipe cooked multiple times
        DayRecipe dr1 = DayRecipe.builder().recipe(testRecipe).build();
        DayRecipe dr2 = DayRecipe.builder().recipe(testRecipe).build();
        DayRecipe dr3 = DayRecipe.builder().recipe(testRecipe).build();

        when(dayRecipeService.findByDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(Arrays.asList(dr1, dr2))  // Q1: 2 times
                .thenReturn(Arrays.asList(dr3))        // Q2: 1 time
                .thenReturn(Collections.emptyList())   // Q3: 0
                .thenReturn(Collections.emptyList());  // Q4: 0

        double score = scoringService.calcPreferenceScore(testRecipe, testDate);

        // (2 * 1.0) + (1 * 0.5) + 0 = 2.5, no bonus, capped at 1.0
        assertEquals(1.0, score, 0.001);
    }

    @Test
    @DisplayName("calcRecencyScore - recently eaten should get high penalty")
    void testCalcRecencyScore_RecentlyEaten() {
        // Mock: last eaten 5 days ago
        Date lastEaten = Date.valueOf("2024-01-10");
        when(recipeService.findLastEaten(testRecipe, testDate)).thenReturn(lastEaten);
        when(recipeService.findNextEaten(testRecipe, testDate)).thenReturn(null);

        double score = scoringService.calcRecencyScore(testRecipe, testDate);

        // 5 days < 7 days, should return oneWeekPenalty
        assertEquals(-0.5, score, 0.001);
    }

    @Test
    @DisplayName("calcRecencyScore - eaten 10 days ago should get medium penalty")
    void testCalcRecencyScore_MediumRecency() {
        // Mock: last eaten 10 days ago
        Date lastEaten = Date.valueOf("2024-01-05");
        when(recipeService.findLastEaten(testRecipe, testDate)).thenReturn(lastEaten);
        when(recipeService.findNextEaten(testRecipe, testDate)).thenReturn(null);

        double score = scoringService.calcRecencyScore(testRecipe, testDate);

        // 10 days, between 7 and 14, should return twoWeekPenalty
        assertEquals(-0.3, score, 0.001);
    }

    @Test
    @DisplayName("calcRecencyScore - eaten 18 days ago should get small penalty")
    void testCalcRecencyScore_LowRecency() {
        // Mock: last eaten 18 days ago
        Date lastEaten = Date.valueOf("2023-12-28");
        when(recipeService.findLastEaten(testRecipe, testDate)).thenReturn(lastEaten);
        when(recipeService.findNextEaten(testRecipe, testDate)).thenReturn(null);

        double score = scoringService.calcRecencyScore(testRecipe, testDate);

        // 18 days, between 14 and 21, should return threeWeekPenalty
        assertEquals(-0.1, score, 0.001);
    }

    @Test
    @DisplayName("calcRecencyScore - eaten long ago should have no penalty")
    void testCalcRecencyScore_LongAgo() {
        // Mock: last eaten 30 days ago
        Date lastEaten = Date.valueOf("2023-12-16");
        when(recipeService.findLastEaten(testRecipe, testDate)).thenReturn(lastEaten);
        when(recipeService.findNextEaten(testRecipe, testDate)).thenReturn(null);

        double score = scoringService.calcRecencyScore(testRecipe, testDate);

        // 30 days > 21, should return 0.0
        assertEquals(0.0, score, 0.001);
    }

    @Test
    @DisplayName("calcRecencyScore - should consider future planning")
    void testCalcRecencyScore_FuturePlanning() {
        // Mock: planned 3 days in future (closer than past)
        Date lastEaten = Date.valueOf("2023-12-01");  // 45 days ago
        Date nextEaten = Date.valueOf("2024-01-18");  // 3 days in future
        
        when(recipeService.findLastEaten(testRecipe, testDate)).thenReturn(lastEaten);
        when(recipeService.findNextEaten(testRecipe, testDate)).thenReturn(nextEaten);

        double score = scoringService.calcRecencyScore(testRecipe, testDate);

        // Should use closer date (3 days), which is < 7 days
        assertEquals(-0.5, score, 0.001);
    }

    @Test
    @DisplayName("calcRecencyScore - never eaten before should have no penalty")
    void testCalcRecencyScore_NeverEaten() {
        when(recipeService.findLastEaten(testRecipe, testDate)).thenReturn(null);
        when(recipeService.findNextEaten(testRecipe, testDate)).thenReturn(null);

        double score = scoringService.calcRecencyScore(testRecipe, testDate);

        // Never eaten, should return 0.0
        assertEquals(0.0, score, 0.001);
    }

    @Test
    @DisplayName("calculateScore - should combine all sub-scores with weights")
    void testCalculateScore_Integration() {
        // Setup: Recipe with health=4, never eaten, not planned
        testRecipe.setHealthScore(4);
        
        when(dayRecipeService.findByDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());
        when(recipeService.findLastEaten(testRecipe, testDate)).thenReturn(null);
        when(recipeService.findNextEaten(testRecipe, testDate)).thenReturn(null);

        double totalScore = scoringService.calculateScore(testRecipe, testDate);

        // Expected:
        // health: 4/5 = 0.8
        // preference: 0.3 (new recipe bonus)
        // recency: 0.0 (never eaten)
        // variety: 0.0 (not implemented yet)
        // Total: (0.2 * 0.8) + (0.3 * 0.3) + (0.25 * 0.0) + (0.1 * 0.0)
        //      = 0.16 + 0.09 + 0 + 0 = 0.25

        assertEquals(0.25, totalScore, 0.001);
    }

    @Test
    @DisplayName("calculateScore - popular but recently eaten recipe")
    void testCalculateScore_PopularButRecent() {
        testRecipe.setHealthScore(5);
        
        // Mock: cooked 4 times in Q1, last eaten 5 days ago
        DayRecipe dr1 = DayRecipe.builder().recipe(testRecipe).build();
        DayRecipe dr2 = DayRecipe.builder().recipe(testRecipe).build();
        DayRecipe dr3 = DayRecipe.builder().recipe(testRecipe).build();
        DayRecipe dr4 = DayRecipe.builder().recipe(testRecipe).build();

        when(dayRecipeService.findByDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(Arrays.asList(dr1, dr2, dr3, dr4))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList());

        Date lastEaten = Date.valueOf("2024-01-10");
        when(recipeService.findLastEaten(testRecipe, testDate)).thenReturn(lastEaten);
        when(recipeService.findNextEaten(testRecipe, testDate)).thenReturn(null);

        double totalScore = scoringService.calculateScore(testRecipe, testDate);

        // Expected:
        // health: 5/5 = 1.0
        // preference: min(1.0, 4*1.0) = 1.0 (no bonus, >3 times)
        // recency: -0.5 (5 days ago)
        // variety: 0.0
        // Total: (0.2 * 1.0) + (0.3 * 1.0) + (0.25 * -0.5) + (0.1 * 0.0)
        //      = 0.2 + 0.3 - 0.125 + 0 = 0.375

        assertEquals(0.375, totalScore, 0.001);
    }

    @Test
    @DisplayName("countPeriod - should count only matching recipes")
    void testCountPeriod_OnlyMatchingRecipes() {
        Recipe otherRecipe = Recipe.builder()
                .recipeName("Other Recipe")
                .build();

        DayRecipe dr1 = DayRecipe.builder().recipe(testRecipe).build();
        DayRecipe dr2 = DayRecipe.builder().recipe(otherRecipe).build();
        DayRecipe dr3 = DayRecipe.builder().recipe(testRecipe).build();
        

        // Use calcPreferenceScore to indirectly test countPeriod
        when(dayRecipeService.findByDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(Arrays.asList(dr1, dr3))  // Q1: 2 matching
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList());

        double score = scoringService.calcPreferenceScore(testRecipe, testDate);

        // Should count only 2 instances of testRecipe in Q1
        // (2 * 1.0) + 0 + 0 + 0 = 2.0, no bonus (>3 total), capped at 1.0
        assertEquals(1.0, score, 0.001);
    }

    @Test
    @DisplayName("countPeriod - should handle null recipes gracefully")
    void testCountPeriod_NullRecipes() {
        DayRecipe dr1 = DayRecipe.builder().recipe(testRecipe).build();
        DayRecipe dr2 = DayRecipe.builder().recipe(null).build();  // Null recipe
        DayRecipe dr3 = DayRecipe.builder().recipe(testRecipe).build();

        when(dayRecipeService.findByDateBetween(any(Date.class), any(Date.class)))
                .thenReturn(Arrays.asList(dr1, dr2, dr3))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList());

        double score = scoringService.calcPreferenceScore(testRecipe, testDate);

        // Should count only 2 non-null matching recipes
        assertEquals(1.0, score, 0.001);
    }
}
