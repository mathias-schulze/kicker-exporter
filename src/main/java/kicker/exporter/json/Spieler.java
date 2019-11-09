package kicker.exporter.json;

public class Spieler implements Comparable<Spieler> {
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int compareTo(Spieler anotherSpieler) {
		return name.compareTo(anotherSpieler.getName());
	}
}