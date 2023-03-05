package nl.jemaja.weekmenu.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Settings {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private PlanRule defaultRule;
	private PlanRule monday;
	private PlanRule tuesday;
	private PlanRule wednesday;
	private PlanRule thursday;
	private PlanRule friday;
	private PlanRule saturday;
	private PlanRule sunday;
	private int healthyThreshold;
	
	

}
