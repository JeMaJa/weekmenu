/**
 * 
 */
package nl.jemaja.weekmenu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.jemaja.weekmenu.model.RecipeStatus;

import java.sql.Date;

/**
 * @author yannick.tollenaere
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayRecipeDto {
	

	private Date date;
	private Long recipeId;
	private String recipeName;
	private RecipeStatus status;
	private Long id;
}
