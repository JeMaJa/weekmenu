package nl.jemaja.weekmenu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeLabel;
import nl.jemaja.weekmenu.model.Settings;

/**
 * Unit tests for RecipeScoringService - Variety Score
 * 
 * @author Yannick / JeMaJa
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RecipeScoringService - Variety Score Tests")
class RecipeScoringServiceVarietyTests {

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
    private RecipeLabel fishLabel;
    private RecipeLabel pastaLabel;
    private RecipeLabel beefLabel;

    @BeforeEach
    void setUp() {
        // Create test settings
        testSettings = Settings.builder()
                .healthWeight(0.2)
                .preferenceWeight(0.3)
                .recencyWeight(0.25)
                .variatyWeight(0.1)
                .build();

        // Create labels
        fishLabel = new RecipeLabel();
        fishLabel.setName("fish");
        
        pastaLabel = new RecipeLabel();
        pastaLabel.setName("pasta");
        
        beefLabel = new RecipeLabel();
        beefLabel.setName("beef");

        // Create test recipe with fish label
        testRecipe = Recipe.builder()
                .recipeName("Salmon")
                .healthScore(4)
                .labels(Arrays.asList(fishLabel))
                .build();

        // Test date: 2024-01-15 (Monday)
        testDate = Date.valueOf("2024-01-15");

        lenient().when(settingsService.getSettings()).thenReturn(testSettings);
    }

    @Test
    @DisplayName("calcVarietyScore - empty week should return perfect score")
    void testCalcVarietyScore_EmptyWeek() {
        List<DayRecipe> emptyWeek = Collections.emptyList();

        double score = scoringService.calcVarietyScore(testRecipe, testDate, emptyWeek);

        assertEquals(1.0, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - same recipe in week should return 0")
    void testCalcVarietyScore_SameRecipe() {
        // Create a day with the same recipe
        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);
        cal.add(Calendar.DATE, -2);
        Date pastDate = new Date(cal.getTimeInMillis());

        DayRecipe dayRecipe = DayRecipe.builder()
                .date(pastDate)
                .recipe(testRecipe)  // Same recipe
                .build();

        List<DayRecipe> weekContext = Arrays.asList(dayRecipe);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // Same recipe = impossible
        assertEquals(0.0, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - adjacent day with same label should get high penalty")
    void testCalcVarietyScore_AdjacentDaySameLabel() {
        // Create fish recipe for adjacent day
        Recipe otherFishRecipe = Recipe.builder()
                .recipeName("Tuna")
                .healthScore(4)
                .labels(Arrays.asList(fishLabel))
                .build();

        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);
        cal.add(Calendar.DATE, -1);  // Yesterday
        Date adjacentDate = new Date(cal.getTimeInMillis());

        DayRecipe dayRecipe = DayRecipe.builder()
                .date(adjacentDate)
                .recipe(otherFishRecipe)
                .build();

        List<DayRecipe> weekContext = Arrays.asList(dayRecipe);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // Adjacent day with same label: penalty 0.4
        // Score: 1.0 - 0.4 = 0.6
        assertEquals(0.6, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - 3 days away with same label should get medium penalty")
    void testCalcVarietyScore_ThreeDaysAwaySameLabel() {
        // Create fish recipe for 3 days ago
        Recipe otherFishRecipe = Recipe.builder()
                .recipeName("Cod")
                .healthScore(4)
                .labels(Arrays.asList(fishLabel))
                .build();

        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);
        cal.add(Calendar.DATE, -3);
        Date pastDate = new Date(cal.getTimeInMillis());

        DayRecipe dayRecipe = DayRecipe.builder()
                .date(pastDate)
                .recipe(otherFishRecipe)
                .build();

        List<DayRecipe> weekContext = Arrays.asList(dayRecipe);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // 3 days with same label: penalty 0.2
        // Score: 1.0 - 0.2 = 0.8
        assertEquals(0.8, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - 5 days away with same label should get small penalty")
    void testCalcVarietyScore_FiveDaysAwaySameLabel() {
        // Create fish recipe for 5 days ago
        Recipe otherFishRecipe = Recipe.builder()
                .recipeName("Halibut")
                .healthScore(4)
                .labels(Arrays.asList(fishLabel))
                .build();

        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);
        cal.add(Calendar.DATE, -5);
        Date pastDate = new Date(cal.getTimeInMillis());

        DayRecipe dayRecipe = DayRecipe.builder()
                .date(pastDate)
                .recipe(otherFishRecipe)
                .build();

        List<DayRecipe> weekContext = Arrays.asList(dayRecipe);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // 5 days with same label: penalty 0.1
        // Score: 1.0 - 0.1 = 0.9
        assertEquals(0.9, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - more than 5 days away with same label should have no penalty")
    void testCalcVarietyScore_DistantSameLabel() {
        // Create fish recipe for 7 days ago
        Recipe otherFishRecipe = Recipe.builder()
                .recipeName("Trout")
                .healthScore(4)
                .labels(Arrays.asList(fishLabel))
                .build();

        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);
        cal.add(Calendar.DATE, -7);
        Date pastDate = new Date(cal.getTimeInMillis());

        DayRecipe dayRecipe = DayRecipe.builder()
                .date(pastDate)
                .recipe(otherFishRecipe)
                .build();

        List<DayRecipe> weekContext = Arrays.asList(dayRecipe);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // 7 days away: no penalty
        assertEquals(1.0, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - different labels should have no penalty")
    void testCalcVarietyScore_DifferentLabels() {
        // Create pasta recipe (different label)
        Recipe pastaRecipe = Recipe.builder()
                .recipeName("Spaghetti")
                .healthScore(3)
                .labels(Arrays.asList(pastaLabel))
                .build();

        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);
        cal.add(Calendar.DATE, -1);  // Yesterday
        Date adjacentDate = new Date(cal.getTimeInMillis());

        DayRecipe dayRecipe = DayRecipe.builder()
                .date(adjacentDate)
                .recipe(pastaRecipe)
                .build();

        List<DayRecipe> weekContext = Arrays.asList(dayRecipe);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // Different labels, no penalty
        assertEquals(1.0, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - multiple same labels should accumulate penalties")
    void testCalcVarietyScore_MultipleSameLabels() {
        // Create fish recipes on different days
        Recipe fish1 = Recipe.builder()
                .recipeName("Tuna")
                .labels(Arrays.asList(fishLabel))
                .build();

        Recipe fish2 = Recipe.builder()
                .recipeName("Cod")
                .labels(Arrays.asList(fishLabel))
                .build();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(testDate);
        cal1.add(Calendar.DATE, -1);  // Yesterday - penalty 0.4
        Date date1 = new Date(cal1.getTimeInMillis());

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(testDate);
        cal2.add(Calendar.DATE, 2);  // 2 days ahead - penalty 0.2
        Date date2 = new Date(cal2.getTimeInMillis());

        DayRecipe day1 = DayRecipe.builder()
                .date(date1)
                .recipe(fish1)
                .build();

        DayRecipe day2 = DayRecipe.builder()
                .date(date2)
                .recipe(fish2)
                .build();

        List<DayRecipe> weekContext = Arrays.asList(day1, day2);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // Total penalty: 0.4 + 0.2 = 0.6
        // Score: 1.0 - 0.6 = 0.4
        assertEquals(0.4, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - should handle null recipes gracefully")
    void testCalcVarietyScore_NullRecipe() {
        // Create a day with null recipe (empty day)
        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);
        cal.add(Calendar.DATE, -1);
        Date pastDate = new Date(cal.getTimeInMillis());

        DayRecipe emptyDay = DayRecipe.builder()
                .date(pastDate)
                .recipe(null)  // No recipe planned
                .build();

        List<DayRecipe> weekContext = Arrays.asList(emptyDay);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // Should skip null recipe, no penalty
        assertEquals(1.0, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - should handle null labels gracefully")
    void testCalcVarietyScore_NullLabels() {
        // Create recipe without labels
        Recipe noLabelRecipe = Recipe.builder()
                .recipeName("Mystery Dish")
                .healthScore(3)
                .labels(null)
                .build();

        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);
        cal.add(Calendar.DATE, -1);
        Date pastDate = new Date(cal.getTimeInMillis());

        DayRecipe dayRecipe = DayRecipe.builder()
                .date(pastDate)
                .recipe(noLabelRecipe)
                .build();

        List<DayRecipe> weekContext = Arrays.asList(dayRecipe);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // Should skip when labels are null, no penalty
        assertEquals(1.0, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - should handle candidate with no labels")
    void testCalcVarietyScore_CandidateNoLabels() {
        // Create candidate without labels
        Recipe candidateNoLabels = Recipe.builder()
                .recipeName("Simple Dish")
                .healthScore(3)
                .labels(null)
                .build();

        // Create planned recipe with labels
        Recipe fishRecipe = Recipe.builder()
                .recipeName("Salmon")
                .labels(Arrays.asList(fishLabel))
                .build();

        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);
        cal.add(Calendar.DATE, -1);
        Date pastDate = new Date(cal.getTimeInMillis());

        DayRecipe dayRecipe = DayRecipe.builder()
                .date(pastDate)
                .recipe(fishRecipe)
                .build();

        List<DayRecipe> weekContext = Arrays.asList(dayRecipe);

        double score = scoringService.calcVarietyScore(candidateNoLabels, testDate, weekContext);

        // Should skip when candidate has no labels, no penalty
        assertEquals(1.0, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - penalty should not go below 0")
    void testCalcVarietyScore_PenaltyCappedAtZero() {
        // Create many fish recipes on adjacent days
        Recipe fish1 = Recipe.builder().recipeName("Fish1").labels(Arrays.asList(fishLabel)).build();
        Recipe fish2 = Recipe.builder().recipeName("Fish2").labels(Arrays.asList(fishLabel)).build();
        Recipe fish3 = Recipe.builder().recipeName("Fish3").labels(Arrays.asList(fishLabel)).build();
        Recipe fish4 = Recipe.builder().recipeName("Fish4").labels(Arrays.asList(fishLabel)).build();

        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);

        DayRecipe day1 = DayRecipe.builder()
                .date(new Date(cal.getTimeInMillis() - 86400000L))  // -1 day
                .recipe(fish1)
                .build();

        DayRecipe day2 = DayRecipe.builder()
                .date(new Date(cal.getTimeInMillis() + 86400000L))  // +1 day
                .recipe(fish2)
                .build();

        DayRecipe day3 = DayRecipe.builder()
                .date(new Date(cal.getTimeInMillis() - 2 * 86400000L))  // -2 days
                .recipe(fish3)
                .build();

        DayRecipe day4 = DayRecipe.builder()
                .date(new Date(cal.getTimeInMillis() + 2 * 86400000L))  // +2 days
                .recipe(fish4)
                .build();

        List<DayRecipe> weekContext = Arrays.asList(day1, day2, day3, day4);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // Total penalty would be: 0.4 + 0.4 + 0.2 + 0.2 = 1.2
        // But score is capped at 0.0
        assertEquals(0.0, score, 0.001);
    }

    @Test
    @DisplayName("calcVarietyScore - should consider future planned days")
    void testCalcVarietyScore_FuturePlanning() {
        // Create fish recipe planned in the future
        Recipe futureFish = Recipe.builder()
                .recipeName("Future Tuna")
                .labels(Arrays.asList(fishLabel))
                .build();

        Calendar cal = Calendar.getInstance();
        cal.setTime(testDate);
        cal.add(Calendar.DATE, 2);  // 2 days in future
        Date futureDate = new Date(cal.getTimeInMillis());

        DayRecipe futureDay = DayRecipe.builder()
                .date(futureDate)
                .recipe(futureFish)
                .build();

        List<DayRecipe> weekContext = Arrays.asList(futureDay);

        double score = scoringService.calcVarietyScore(testRecipe, testDate, weekContext);

        // 2 days distance with same label: penalty 0.2
        // Score: 1.0 - 0.2 = 0.8
        assertEquals(0.8, score, 0.001);
    }
}
