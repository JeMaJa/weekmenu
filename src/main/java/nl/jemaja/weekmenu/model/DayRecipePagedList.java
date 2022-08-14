package nl.jemaja.weekmenu.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;



public class DayRecipePagedList extends PageImpl<DayRecipe> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4614222274875646553L;

	public DayRecipePagedList(List<DayRecipe> content) {
		super(content);
		// TODO Auto-generated constructor stub
	}
	

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DayRecipePagedList(@JsonProperty("content") List<DayRecipe> content,
                         @JsonProperty("number") int number,
                         @JsonProperty("size") int size,
                         @JsonProperty("totalElements") Long totalElements,
                         @JsonProperty("pageable") JsonNode pageable,
                         @JsonProperty("last") boolean last,
                         @JsonProperty("totalPages") int totalPages,
                         @JsonProperty("sort") JsonNode sort,
                         @JsonProperty("first") boolean first,
                         @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }


	public DayRecipePagedList(List<DayRecipe> collect, PageRequest of, long totalElements) {
		super(collect, of, totalElements);
	}

}
