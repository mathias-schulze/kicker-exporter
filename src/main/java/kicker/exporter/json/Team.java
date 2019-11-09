package kicker.exporter.json;

import java.util.Map;
import java.util.TreeMap;

public class Team implements Comparable<Team> {
	
	private int id;
	private Map<String, Spieler> spieler = new TreeMap<>();
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Map<String, Spieler> getSpieler() {
		return spieler;
	}
	
	public void setSpieler(Map<String, Spieler> spieler) {
		this.spieler = spieler;
	}
	
	public void addSpieler(Spieler spieler) {
		this.spieler.put(spieler.getName(), spieler);
	}
	
	@Override
	public int compareTo(Team anotherTeam) {
		return Integer.compare(getId(), anotherTeam.getId());
	}
}