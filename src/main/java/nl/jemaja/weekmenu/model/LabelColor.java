package nl.jemaja.weekmenu.model;

public enum LabelColor {
	GREEN("green","btn btn-outline-success btn-sm"),
	RED("red","btn btn-outline-danger btn-sm"),
	YELLOW("yellow","btn btn-outline-warning btn-sm"),
	BLUE("blue","btn btn-outline-primary btn-sm"),
	GREY("grey","btn btn-outline-secondary btn-sm");

	LabelColor(String color, String buttonClass) {
		this.color = color;
		this.buttonClass = buttonClass;
	}
	
	private String color;
	private String buttonClass;
	
	public String getButtonClass() {
		return this.buttonClass;
	}
}
/*
<button type="button" class="btn btn-outline-primary">Primary</button>
<button type="button" class="btn btn-outline-secondary">Secondary</button>
<button type="button" class="btn btn-outline-success">Success</button>
<button type="button" class="btn btn-outline-danger">Danger</button>
<button type="button" class="btn btn-outline-warning">Warning</button>
<button type="button" class="btn btn-outline-info">Info</button>
<button type="button" class="btn btn-outline-light">Light</button>
<button type="button" class="btn btn-outline-dark">Dark</button>
*/