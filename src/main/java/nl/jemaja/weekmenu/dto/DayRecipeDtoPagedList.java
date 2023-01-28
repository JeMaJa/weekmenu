/**
 * 
 */
package nl.jemaja.weekmenu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

/**
 * @author yannick.tollenaere
 *
 */
public class DayRecipeDtoPagedList extends PageImpl<DayRecipeDto> implements Serializable {

	public DayRecipeDtoPagedList(List<DayRecipeDto> content, Pageable pageable, long total) {
		super(content, pageable, total);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8500949615188494830L;
	public DayRecipeDtoPagedList(List<DayRecipeDto> content) {
		super(content);
	}
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DayRecipeDtoPagedList(@JsonProperty("content") List<DayRecipeDto> content,
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

	

}
