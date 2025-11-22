package nl.jemaja.weekmenu.service;

import nl.jemaja.weekmenu.model.DayRecipe;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.sql.Date;

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
        double variaty = calcVariatyScore();
        return (settings.getHealthWeight() * health) +
                (settings.getPreferenceWeight() * preference) +
                (settings.getRecencyWeight() * recency) +
                (settings.getVariatyWeight() * variaty);
    }

     double calcVariatyScore() {
        return 0;
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
        for (DayRecipe dayRecipe : dayRecipeService.findByDateBetween((java.sql.Date) start, (java.sql.Date) end)) {
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
