package nl.jemaja.weekmenu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.scheduling.support.CronExpression;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

/*
 * Class: Rule
 * Purpose: Hold fixed rules with a CronExpression to control the planning
 * Types of rules: 
 * 		fixed recipe, will set a fixed recipe on the dates matching the cron experession
 * 		Labels: search for recipe matching labels, 
 * 		maxComplexity: only recipes having complexity below the max
 * 		minHealthscore
 */

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Rule {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@CreationTimestamp
    private OffsetDateTime createdDate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
	
	private String cronString;
	@Column(unique=true)
	private String name;
	private String description;
	@Column(columnDefinition = "int default 5")
	private int priority;
	
	@ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name = "rule_labels",
        joinColumns = {@JoinColumn(name = "rule_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "label_id", referencedColumnName = "id")})
    private List<RecipeLabel> labels;
	@Column(columnDefinition = "int default 5")
	private int maxComplexity;
	
	@Column(columnDefinition = "int default 1")
	private int minHealthScore;
	
	
	private long recipeId; //fixed recipe for this day
	
	private boolean overwriteSuggested; // overwrite suggested recipes
	
	public CronExpression getCronExpression() {
		return CronExpression.parse(cronString);
		//much info: http://www.quartz-scheduler.org/api/2.1.7/org/quartz/CronExpression.html#getNextValidTimeAfter(java.util.Date)
		
	}
	
	
}
