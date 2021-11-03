package ioutils;

import org.jdom2.Element;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CSVReader extends Thread {
	private final String OUTPUT_URI;
	private final String INPUT_URI;
	private String rootElementName;
	private List<String> headersList;

	public CSVReader (String rootElementName, String inputURI, String outputURI) {
		this.INPUT_URI = inputURI;
		this.OUTPUT_URI = outputURI;
		this.rootElementName = rootElementName;
	}

	public void run() {
		try {
			this.loadCSVData();
		}catch(IOException e) {
			System.err.println("XML Files could not be written from csv");
		}
	}
	public void loadCSVData() throws IOException {
		JDOM jdomController = new JDOM(this.INPUT_URI);
		jdomController.initDocument();
		jdomController.getDocument().setRootElement(new Element(this.rootElementName));
		fillDocumentDataFromCSV(jdomController);
		jdomController.writeXMLFile(this.OUTPUT_URI);
	}

	public void fillDocumentDataFromCSV(JDOM jdomController) throws IOException {
		Stream<String> dataStream = Files.lines(Path.of(this.INPUT_URI), Charset.forName("windows-1252"));
		dataStream.forEach(s -> {
			if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
				addLineElement(s, jdomController);
			}else{
				headersList = Arrays.asList(s.split(";"));
			}
		});
	}
	public void addLineElement(String line, JDOM jdomController) {
		Element measure = new Element("measure");
		Element root = jdomController.getDocument().getRootElement();
		List<String> splitted = Arrays.asList(line.split(";"));
		for(int i = 0; i < splitted.size(); i++)
			measure.addContent(Optional.of(new Element(headersList.get(i)).setText(splitted.get(i))).orElse(null));
		root.addContent(measure);
	}

	public static void generateXMLFilesFromCSV() throws IOException {
		CSVReader csvPollution = new CSVReader( "contaminacion", "data//calidad_aire_datos_mes.csv", "data//calidad_aire_datos_mes.xml");
		CSVReader csvTemperature = new CSVReader( "temperatura", "data//calidad_aire_datos_meteo_mes.csv", "data//calidad_aire_datos_meteo_mes.xml");
		CSVReader csvStations = new CSVReader( "estaciones", "data/calidad_aire_estaciones.csv", "data/calidad_aire_estaciones.xml");
		CSVReader csvMagnitudesPollution = new CSVReader( "magnitudes", "data/magnitudes_aire.csv", "data/magnitudes_aire.xml");
		CSVReader csvMagnitudesTemperature = new CSVReader( "magnitudes", "data/magnitudes_aire_meteo.csv", "data/magnitudes_aire_meteo.xml");
		csvPollution.loadCSVData();
		csvTemperature.loadCSVData();
		csvStations.loadCSVData();
		csvMagnitudesPollution.loadCSVData();
		csvMagnitudesTemperature.loadCSVData();

	}
}
