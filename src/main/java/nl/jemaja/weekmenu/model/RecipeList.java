/**
 * 
 */
package nl.jemaja.weekmenu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yannick.tollenaere
 *
 */
public class RecipeList {

	private List<Recipe> recipes = new ArrayList();

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

	@Override
	public String toString() {
		return "RecipeList [recipes=" + recipes + "]";
	}
	
}
