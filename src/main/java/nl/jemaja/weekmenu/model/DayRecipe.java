/**
 * 
 */
package nl.jemaja.weekmenu.model;



import java.sql.Date;

/**
 * @author yannick.tollenaere
 *
 */

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder

@Data
public class DayRecipe {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;
	private Date date;
	@ManyToOne
	private Recipe recipe;
	

}
