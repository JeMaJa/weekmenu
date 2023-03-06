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
import java.util.List;

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
	
	@ManyToMany(cascade=CascadeType.ALL, mappedBy="ingredients")
	List<Recipe> recipes;

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
