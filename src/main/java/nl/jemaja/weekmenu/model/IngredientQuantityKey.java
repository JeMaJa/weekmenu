package nl.jemaja.weekmenu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class IngredientQuantityKey implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -8726832852545584799L;

	@Column(name = "recipe_id")
	 private long recipeId;
	 
	@Column(name = "ingredient_id")
	 private long ingredientId;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IngredientQuantityKey other = (IngredientQuantityKey) obj;
		if (ingredientId != other.ingredientId)
			return false;
		if (recipeId != other.recipeId)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ingredientId ^ (ingredientId >>> 32));
		result = prime * result + (int) (recipeId ^ (recipeId >>> 32));
		return result;
	}
}
