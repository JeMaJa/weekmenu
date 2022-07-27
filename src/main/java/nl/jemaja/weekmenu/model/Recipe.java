package nl.jemaja.weekmenu.model;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Entity;
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
@Entity
public class Recipe {
	@Id
    @GeneratedValue
	private long recipeId;
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
    private String recipeName;
    private String externalUrl; // URL of external recipe file
    private String description;
    private boolean vega;
    private boolean workdayOk; // determines if this is a recipe is suitable for workdays
    private Complexity complexity;
    

}
