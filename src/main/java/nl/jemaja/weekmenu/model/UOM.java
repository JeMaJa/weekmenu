package nl.jemaja.weekmenu.model;

public enum UOM {
	PIECE("piece","pieces"),
	GRAM("gram","grams"),
	KG("kilogram","kilograms"),
	LITRE("liter","liters"),
	ML("milliliter","milliliter"),
	SPOON("spoon","spoons"),
	TABLESPOON("tablespoon","tablespoon");

	UOM(String single, String plural) {
		this.single = single;
		this.plural = plural;
	}
	private String single;
	private String plural;
	
}
