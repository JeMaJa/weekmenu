package nl.jemaja.weekmenu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private UOM uom;
	@Transient 
	private boolean update; //used for DTO purposes, field is transient so not stored in the db.
	
	/*
	@JsonIgnore
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="ingredients")
	List<Recipe> recipes;
	*/
	
	//https://www.baeldung.com/jpa-many-to-many
	@JsonIgnore
	@OneToMany(mappedBy = "ingredient")
	Set<IngredientQuantity> ingredientQuantities;
	
	public void update(Ingredient ingredient) {
		this.name = ingredient.getName();
		this.uom = ingredient.getUom();
		
	}

	public boolean validate() {
		if(this.name != null && name.length() > 0 && this.uom != null) {
		return true;
		} 
		return false;
		
	}
	

}
