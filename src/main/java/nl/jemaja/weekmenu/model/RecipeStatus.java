package nl.jemaja.weekmenu.model;

public enum RecipeStatus {

    NOT_SET(0),
    SUGGESTED(1),
    SELECTED(2);

    private int value;

    RecipeStatus(int i) {
        this.setValue(i);
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
