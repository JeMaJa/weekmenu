package nl.jemaja.weekmenu.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
