package nl.jemaja.weekmenu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

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
	@Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    
	//taken out, we will not store it on the recipe but get it from the db!
	//@Temporal(TemporalType.TIMESTAMP)
	//private Date lastEaten;

    
    @Column(unique=true)
    private String recipeName;
    private String externalUrl; // URL of external recipe file
    
    @Column(columnDefinition="LONGTEXT")
    private String description;
   
    @Column(columnDefinition = "boolean default false")
    private boolean vega;
    @Column(columnDefinition = "boolean default true")
    private boolean workdayOk; // determines if this is a recipe is suitable for workdays
    @Column(columnDefinition = "boolean default false")
	private boolean coolDown; // if this is true we will eat it less often
    @Column(columnDefinition = "boolean default false")
	private boolean active; // determines if a recipe can be eaten
    @Column(columnDefinition = "int default 3")
    private int complexity;
    @Column(columnDefinition = "int default 3")
    private int healthScore;
    @Column(columnDefinition = "int default 4")
    private int servings;  // number of servings in default recipe.
    private String imageUrl;
    @Column(columnDefinition="LONGTEXT")
    private String shortDescription;
    
    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
        name = "recipe_labels",
        joinColumns = {@JoinColumn(name = "recipe_id", referencedColumnName = "recipeId")},
        inverseJoinColumns = {@JoinColumn(name = "label_id", referencedColumnName = "id")})
    private List<RecipeLabel> labels;
    
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
        name = "recipe_ingredients",
        joinColumns = {@JoinColumn(name = "recipe_id", referencedColumnName = "recipeId")},
        inverseJoinColumns = {@JoinColumn(name = "ingredient_id", referencedColumnName = "id")})
    private List<Ingredient> ingredients;
    
    
   
	
    
    
    

}
