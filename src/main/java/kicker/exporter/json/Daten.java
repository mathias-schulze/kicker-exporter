package kicker.exporter.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Daten {
	
	private Map<String, Spieler> spieler = new TreeMap<>();
	
	private Map<Integer, Team> teams = new TreeMap<>();
	
	private List<Spiel> spiele = new ArrayList<>();
	
	public Map<String, Spieler> getSpieler() {
		return spieler;
	}
	
	public void setSpieler(Map<String, Spieler> spieler) {
		this.spieler = spieler;
	}
	
	public void addSpieler(Spieler spieler) {
		this.spieler.put(spieler.getName(), spieler);
	}
	
	public Map<Integer, Team> getTeams() {
		return teams;
	}
	
	public void setTeams(Map<Integer, Team> teams) {
		this.teams = teams;
	}
	
	public void addTeam(Team team) {
		this.teams.put(team.getId(), team);
	}
	
	public List<Spiel> getSpiele() {
		return spiele;
	}
	
	public void setSpiele(List<Spiel> spiele) {
		this.spiele = spiele;
	}
	
	public void addSpiel(Spiel spiel) {
		this.spiele.add(spiel);
	}
}