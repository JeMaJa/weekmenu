package nl.jemaja.weekmenu.model;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	
	@ManyToMany(cascade=CascadeType.MERGE, mappedBy="labels")
	List<Recipe> recipes;
}
