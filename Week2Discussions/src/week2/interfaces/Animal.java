package week2.interfaces;


/*
 * Abstract class representing any animal
 */
public abstract class Animal {
	
	private String name;
	
	private String type;
	
	public Animal(String type, String name) {
		this.type = type;
		this.name = name;		
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}

	public abstract String speak();
	
}
