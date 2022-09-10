/**
 * 
 */
package nl.jemaja.weekmenu.dto;

import java.sql.Date;
import java.time.OffsetDateTime;

import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.jemaja.weekmenu.model.Recipe;

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
	private Boolean workDay;
	private int status;
	private Long id;
}
