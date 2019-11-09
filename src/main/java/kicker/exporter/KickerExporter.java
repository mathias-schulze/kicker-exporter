package kicker.exporter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;

import kicker.exporter.json.Daten;
import kicker.exporter.json.Spiel;
import kicker.exporter.json.Spieler;
import kicker.exporter.json.Team;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KickerExporter {
	
	private static final String KICKER_URL = "https://www.kicker.cool/%s/matches?date=";
	
	private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private static final Pattern TEAM_NO_PATTERN = Pattern.compile("(\\d*)$");
	
	private void export(String liga) throws IOException {
		
		Daten daten = new Daten();
		
		LocalDate datum = LocalDate.of(2019, 1, 1);
		do {
			verarbeite(daten, liga, datum);
			datum = datum.plusDays(1);
		} while (datum.isBefore(LocalDate.now().plusDays(1)));
		
		Collections.sort(daten.getSpiele());
		writeJson(daten);
		
		System.out.println("Anzahl Spieler: " + daten.getSpieler().size());
		System.out.println("Anzahl Teams  : " + daten.getTeams().size());
		System.out.println("Anzahl Spiele : " + daten.getSpiele().size());
	}
	
	private void verarbeite(Daten daten, String liga, LocalDate datum) throws IOException {
		
		String html = getKickerHtml(liga, datum);
		
		Document doc = Jsoup.parse(html);
		Elements matches = doc.select("li[class='m-match']");
		matches.forEach(match -> {
			Spiel spiel = new Spiel();
			spiel.setDatum(datum.format(DATE_PATTERN));
			spiel.setId(Integer.parseInt(match.attr("data-id")));
			
			Element elementWinner = match.select("a[class='m-match--team as-winner']").first();
			Element elementLooser = match.select("a[class='m-match--team']").first();
			Element elementMatchScore = match.select("div[class='m-match--score']").first();
			
			spiel.setSieger(getTeam(daten, elementWinner));
			spiel.setVerlierer(getTeam(daten, elementLooser));
			
			String[] ergebnis = elementMatchScore.ownText().split(":");
			spiel.setToreSieger(Integer.parseInt(ergebnis[0]));
			spiel.setToreVerlierer(Integer.parseInt(ergebnis[1]));
			
			daten.addSpiel(spiel);
		});
	}
	
	private String getKickerHtml(String liga, LocalDate datum) throws IOException {
		
		Request request = new Request.Builder().url(String.format(KICKER_URL, liga) + DATE_PATTERN.format(datum)).build();
		
		OkHttpClient client = new OkHttpClient();
		Call call = client.newCall(request);
		Response response = call.execute();
		
		return response.body().string();
	}
	
	private Team getTeam(Daten daten, Element elementTeam) {
		
		Matcher idMatcher = TEAM_NO_PATTERN.matcher(elementTeam.attr("href"));
		String idText = idMatcher.find() ? idMatcher.group(0) : "0";
		int id = Integer.parseInt(idText);
		
		Team team = daten.getTeams().get(id);
		if (team == null) {
			team = new Team();
			team.setId(id);
			
			team.setSpieler(elementTeam.select("span[class='m-match--team--member']").stream()
					.map(m -> getSpieler(daten, m))
					.collect(Collectors.toMap(Spieler::getName, Function.identity())));
			
			daten.addTeam(team);
		}
		
		return team;
	}
	
	private Spieler getSpieler(Daten daten, Element elementSpieler) {
		
		String name = elementSpieler.attr("title");
		
		Spieler spieler = daten.getSpieler().get(name);
		if (spieler == null) {
			spieler = new Spieler();
			spieler.setName(name);
			
			daten.addSpieler(spieler);
		}
		
		return spieler;
	}
	
	private void writeJson(Daten daten) throws IOException {
		
		ObjectMapper om = new ObjectMapper();
		
		FileWriter fw = new FileWriter("kicker.json", Charset.forName("UTF-8"));
		om.writerWithDefaultPrettyPrinter().writeValue(fw, daten);
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		(new KickerExporter()).export(args[0]);
	}
}
