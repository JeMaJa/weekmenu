
package nl.jemaja.weekmenu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yannick.tollenaere
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDto {
	
	private long recipeId;
    private String recipeName;
    private String externalUrl;
    private String description;
    private boolean vega;
    private boolean workdayOk;
    private boolean cooldDown;
    private int healthScore;
    private int complexity;
    private String imageUrl;
    private int servings;  // number of servings in default recipe.
    private String shortDescription;
    private boolean update; // this boolean is set when updating an existing recipe. 
}
