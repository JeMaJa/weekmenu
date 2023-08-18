package nl.jemaja.weekmenu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IQDto {
	private long ingredientId;
	private long recipeId;
	private float quantity;
	private String recipe;
	private String ingredient;
	private String uom;
	
	public IQDto(String recipe, String ingredient, float quantity, String uom) {
		super();
		this.recipe = recipe;
		this.ingredient = ingredient;
		this.quantity = quantity;
		this.uom = uom;
	}
}
