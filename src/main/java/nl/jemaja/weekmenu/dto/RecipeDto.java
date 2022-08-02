
package nl.jemaja.weekmenu.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.jemaja.weekmenu.model.HealthScore;

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
    private HealthScore healthScore;
}
