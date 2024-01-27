package nl.jemaja.weekmenu.model;

import java.sql.Date;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public enum UOM {
	PIECE("piece","PIECE","a"),
	GRAM("gram","grams","b"),
	KG("kilogram","kilograms","c"),
	LITER("liter","liters","d"),
	ML("milliliter","milliliter","e"),
	SPOON("spoon","spoons","f"),
	TABLESPOON("tablespoon","tablespoon","g");

	private String single;
	private String plural;
	private String nog;
	//UOM(String single, String plural) {
	//	this.single = single;
	//	this.plural = plural;
	//}
	public String getSingle() {
		return single;
	}
	public String getPlural() {
		return plural;
	}
	
	public String getNog() {
		return nog;
	}

	
	//public String toString() {
	//	return this.plural + this.nog;
	//}
}
