package nl.jemaja.weekmenu.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeLabel {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private int sortOrder;
	
	@Column(unique=true)
	private String name;
	
	@JsonIgnore
	@ManyToMany(cascade=CascadeType.MERGE, mappedBy="labels")
	List<Recipe> recipes;
}
