package nl.jemaja.weekmenu.model;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "recipes")
public class Recipe {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long recipeId;
	@CreationTimestamp
    private OffsetDateTime createdDate;
	@UpdateTimestamp
    private OffsetDateTime lastModifiedDate;
    private OffsetDateTime lastEaten;
    
   // @Column(unique=true)
    private String recipeName;
    private String externalUrl; // URL of external recipe file
    private String description;
    private boolean vega;
    private boolean workdayOk; // determines if this is a recipe is suitable for workdays
    private Complexity complexity;
    private HealthScore healthScore;
    
    
    private Ingredient[] ingredients;
    
    
    

}
