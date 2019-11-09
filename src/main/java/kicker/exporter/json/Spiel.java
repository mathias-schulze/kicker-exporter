package kicker.exporter.json;

public class Spiel implements Comparable<Spiel> {
	
	private String datum;
	private int id;
	private Team sieger;
	private Team verlierer;
	private int toreSieger;
	private int toreVerlierer;
	
	public String getDatum() {
		return datum;
	}
	
	public void setDatum(String datum) {
		this.datum = datum;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Team getSieger() {
		return sieger;
	}
	
	public void setSieger(Team sieger) {
		this.sieger = sieger;
	}
	
	public Team getVerlierer() {
		return verlierer;
	}
	
	public void setVerlierer(Team verlierer) {
		this.verlierer = verlierer;
	}
	
	public int getToreSieger() {
		return toreSieger;
	}
	
	public void setToreSieger(int toreSieger) {
		this.toreSieger = toreSieger;
	}
	
	public int getToreVerlierer() {
		return toreVerlierer;
	}
	
	public void setToreVerlierer(int toreVerlierer) {
		this.toreVerlierer = toreVerlierer;
	}
	
	@Override
	public int compareTo(Spiel anotherSpiel) {
		return Integer.compare(getId(), anotherSpiel.getId());
	}
}