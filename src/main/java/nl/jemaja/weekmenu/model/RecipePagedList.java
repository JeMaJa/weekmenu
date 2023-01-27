package nl.jemaja.weekmenu.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class RecipePagedList extends PageImpl<Recipe> implements Serializable{

	public RecipePagedList(List<Recipe> content, Pageable pageable, long total) {
		super(content, pageable, total);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8644521486809669433L;

	public RecipePagedList(List<Recipe> content) {
		super(content);
	}
}
