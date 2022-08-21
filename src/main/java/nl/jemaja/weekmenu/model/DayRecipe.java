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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(false)
public class DayRecipe {
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

}
