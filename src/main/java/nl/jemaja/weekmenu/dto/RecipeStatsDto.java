package nl.jemaja.weekmenu.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Class to hold some statistics on recipes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeStatsDto {
	
	private long recipeId;
	private int timesEaten;
	private Date lastEaten;
	private Date nextEaten;
	private float percentageEaten;

}
