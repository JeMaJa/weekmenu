/**
 *
 */
package nl.jemaja.weekmenu.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.jemaja.weekmenu.service.DayRecipeService;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.sql.Date;
import java.time.OffsetDateTime;

@Component
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(false)
public class DayRecipe implements Comparable<DayRecipe> {


    @Transient
    @Autowired
    DayRecipeService dRService;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    @Column(unique = true)
    private Date date;
    @ManyToOne
    private Recipe recipe;
    private RecipeStatus status;
	private boolean workday;

    @CreationTimestamp
    private OffsetDateTime createdDate;
    @UpdateTimestamp
    private OffsetDateTime lastModifiedDate;


    public int compareTo(DayRecipe dayrecipe) {
        return date.compareTo(dayrecipe.getDate());
    }


}
