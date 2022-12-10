/**
 * 
 */
package nl.jemaja.weekmenu.model;



import java.sql.Date;
import java.time.OffsetDateTime;

import javax.persistence.Cacheable;
import javax.persistence.Column;

/**
 * @author yannick.tollenaere
 *
 */

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.jemaja.weekmenu.repository.DayRecipeRepoImpl;
import nl.jemaja.weekmenu.repository.DayRecipeRepository;
import nl.jemaja.weekmenu.service.DayRecipeService;
import nl.jemaja.weekmenu.service.RecipeService;

@Component
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(false)
public class DayRecipe {
	
	
	@Transient
	@Autowired
	DayRecipeService dRService;
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
	@Column(unique=true)
	private Date date;
	@ManyToOne
	private Recipe recipe;
	private int status; //probably need to make an enum of this null/0 = not set, 1=suggested, 2=Selected
	
	@CreationTimestamp
    private OffsetDateTime createdDate;
	@UpdateTimestamp
    private OffsetDateTime lastModifiedDate;
	private Boolean workday;
	
	public void setRecipe(Recipe newRecipe) {
		
		if(this.recipe != null) {
			
			//update the last eaten
			//DayRecipeRepository dRRepo = new DayRecipeRepoImpl();
			//DayRecipeService dRService = new DayRecipeService(dRRepo);
			//Date prevDate = dRService.findPrevLastEaten(this.recipe,this.date);
			//this.recipe.setLastEaten(prevDate);
		} 
		this.recipe = newRecipe;
	}

}
