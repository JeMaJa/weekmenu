package nl.jemaja.weekmenu.service;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.RecipeLabel;
import nl.jemaja.weekmenu.model.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.min;

/*
author: Yannick / JeMaJa
www.jemaja.nl
www.github.com/jemaja


 */
@Service
public class RecipeScoringService {

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private DayRecipeService dayRecipeService;

    @Autowired
    private RecipeService recipeService;

    public double calculateScore(Recipe recipe, Date date) {

        Settings settings = settingsService.getSettings();
        double health = calcHealthScore(recipe);
        double preference = calcPreferenceScore(recipe, date);
        double recency = calcRecencyScore(recipe, date);

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(date);
        calStart.add(Calendar.DATE, -6);

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(date);
        calEnd.add(Calendar.DATE, 6);

        Date startDate = new Date(calStart.getTimeInMillis());
        Date endDate = new Date(calEnd.getTimeInMillis());

        List<DayRecipe> weekContext = dayRecipeService.findByDateBetween(startDate, endDate);

        double variety = calcVarietyScore(recipe, date, weekContext);
        return (settings.getHealthWeight() * health) +
                (settings.getPreferenceWeight() * preference) +
                (settings.getRecencyWeight() * recency) +
                (settings.getVariatyWeight() * variety);
    }

    double calcVarietyScore(Recipe candidate, Date targetDate, List<DayRecipe> weekContext) {
        double penalty = 0.0;

        // Look at all already-planned days in the week (both past and future)
        for (DayRecipe planned : weekContext) {

            long diffInMillies = Math.abs(planned.getDate().getTime() - targetDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (planned.getRecipe() == null) {
                continue;  // Skip empty days
            }

            // Same recipe = impossible (return 0.0 immediately)
            if (planned.getRecipe().equals(candidate)) {
                return 0.0;
            }
            if (planned.getDate().equals(targetDate)) {
                continue;  // Don't compare with itself
            }



            List<RecipeLabel> candidateLabels = candidate.getLabels();
            List<RecipeLabel> plannedLabels = planned.getRecipe().getLabels();
            if (candidateLabels == null || planned.getRecipe().getLabels() == null) {
                continue;  // Skip if no labels
            }
                // Count shared labels
                long shared = candidateLabels.stream()
                        .filter(plannedLabels::contains)
                        .count();


                if (shared > 0) {
                    // Penalty based on proximity
                    if (diff <= 1) penalty += 0.4;  // Adjacent days
                    else if (diff <= 3) penalty += 0.2;  // Within 3 days
                    else if (diff <= 5) penalty += 0.1;  // Same week
                }
            }


        return Math.max(0.0, 1.0 - penalty);

    }

     double calcRecencyScore(Recipe recipe, Date date) {
        Settings settings = settingsService.getSettings();
        Date last = recipeService.findLastEaten(recipe, date);
        Date next =  recipeService.findNextEaten(recipe, date);
        // Calculate days to closest occurrence (past or future)
        int daysSinceLast = (last != null) ? daysBetween(last, date) : Integer.MAX_VALUE;
        int daysUntilNext = (next != null) ? daysBetween(date, next) : Integer.MAX_VALUE;

        int daysSince = Math.min(daysSinceLast, daysUntilNext);

        // Apply penalty based on proximity
        if (daysSince < 7) return settings.getOneWeekPenalty();
        else if (daysSince < 14) return settings.getTwoWeekPenalty();
        else if (daysSince < 21) return settings.getThreeWeekPenalty();
        else return 0.0;
    }

     double calcHealthScore(Recipe recipe) {
        return (double) recipe.getHealthScore() / 5; // normalized health score 1-5
    }

    /*
    Calculate the preference score based upon seasonality and how often a meal has been cooked in the past 12 months, weighting per quarter
    */
     double calcPreferenceScore(Recipe recipe, Date date) {
        Settings settings = settingsService.getSettings();
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(date);
        calStart.add(Calendar.DATE, -90);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(date);

        // Cast to java.sql.Date
        int qOneCount = countPeriod(recipe, new Date(calStart.getTimeInMillis()), new Date(calEnd.getTimeInMillis()));

        calStart.add(Calendar.DATE, -90);
        calEnd.add(Calendar.DATE, -90);
        int qTwoCount = countPeriod(recipe, new Date(calStart.getTimeInMillis()), new Date(calEnd.getTimeInMillis()));

        calStart.add(Calendar.DATE, -90);
        calEnd.add(Calendar.DATE, -90);
        int qThreeCount = countPeriod(recipe, new Date(calStart.getTimeInMillis()), new Date(calEnd.getTimeInMillis()));

        calStart.add(Calendar.DATE, -90);
        calEnd.add(Calendar.DATE, -90);
        int qFourCount = countPeriod(recipe, new Date(calStart.getTimeInMillis()), new Date(calEnd.getTimeInMillis()));


        /*
        Add a bonus for new recipe's
         */
        int countTotal = qOneCount + qThreeCount + qTwoCount + qFourCount;
        double newRecipeBonus = 0;
        if (countTotal == 0) {
            //new recipe
            newRecipeBonus = 0.3;
        } else if (countTotal <3) {
            newRecipeBonus = 0.15;
        }

        double score = (    qOneCount * settings.getQOneWeight() +
                            qTwoCount * settings.getQTwoWeight() +
                            qThreeCount * settings.getQThreeWeight() +
                            qFourCount * settings.getQFourWeight()
                            +newRecipeBonus);

        return min(1.0, score);
    }

    private int countPeriod(Recipe recipe, Date start, Date end) {
        int count =0;
        for (DayRecipe dayRecipe : dayRecipeService.findByDateBetween(start, end)) {
            if ( dayRecipe.getRecipe() != null && dayRecipe.getRecipe().equals(recipe)){
                count++;            }
        }


        return count;
    }
    private int daysBetween(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
}
