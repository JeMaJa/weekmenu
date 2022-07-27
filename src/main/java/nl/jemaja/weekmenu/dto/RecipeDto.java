
package nl.jemaja.weekmenu.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
	
	private long RecipeId;
    private String RecipeName;
    private String url;

}
