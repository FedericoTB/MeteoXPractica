package ioutils;

import org.jdom2.Element;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Class that parses csv to xml
 * @author sps169, FedericoTB
 */
public class CSVReader extends Thread {
	private final String OUTPUT_URI;
	private final String INPUT_URI;
	private String rootElementName;
	private String tagName;
	private List<String> headersList;

	/**
	 *
	 * @param rootElementName name of the xml root element of this csv
	 * @param tagName name of the registry element of this csv
	 * @param inputURI uri of csv input file
	 * @param outputURI uri of xml output file
	 */
	public CSVReader (String rootElementName, String tagName, String inputURI, String outputURI) {
		this.INPUT_URI = inputURI;
		this.OUTPUT_URI = outputURI;
		this.rootElementName = rootElementName;
		this.tagName = tagName;
	}

	/**
	 * Runs the csv reading
	 */
	public void run() {
		try {
			this.loadCSVData();
		}catch(IOException e) {
			System.err.println("XML Files could not be written from csv");
		}
	}

	/**
	 * Uses jdom to parse the csv to xml
	 * @throws IOException if file creation fails
	 */
	public void loadCSVData() throws IOException {
		JDOM jdomController = new JDOM(this.INPUT_URI);
		jdomController.initDocument();
		jdomController.getDocument().setRootElement(new Element(this.rootElementName));
		fillDocumentDataFromCSV(jdomController);
		jdomController.writeXMLFile(this.OUTPUT_URI);
	}

	/**
	 * reads csv and sends each line to a xml line parser
	 * @param jdomController jdom controller
	 * @throws IOException if file reading fails
	 */
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

	/**
	 * parses a string to xml and introduces it in the dom document
	 * @param line csv string
	 * @param jdomController jdom controller
	 */
	public void addLineElement(String line, JDOM jdomController) {
		Element measure = new Element(this.tagName);
		Element root = jdomController.getDocument().getRootElement();
		List<String> splitted = Arrays.asList(line.split(";"));
		for(int i = 0; i < splitted.size(); i++)
			measure.addContent(Optional.of(new Element(headersList.get(i)).setText(splitted.get(i))).orElse(null));
		root.addContent(measure);
	}

	/**
	 * generates all xml files from their respective csv
	 * @throws IOException if reading/writing fails
	 */
	public static void generateXMLFilesFromCSV() throws IOException {
		CSVReader csvPollution = new CSVReader( "contaminacion", "medicion","data"+ File.separator+"calidad_aire_datos_mes.csv", "data"+ File.separator+"calidad_aire_datos_mes.xml");
		CSVReader csvTemperature = new CSVReader( "temperatura", "medicion","data"+ File.separator+"calidad_aire_datos_meteo_mes.csv", "data"+ File.separator+"calidad_aire_datos_meteo_mes.xml");
		CSVReader csvStations = new CSVReader( "estaciones", "estacion","data"+ File.separator+"calidad_aire_estaciones.csv", "data"+ File.separator+"calidad_aire_estaciones.xml");
		CSVReader csvMagnitudesPollution = new CSVReader( "magnitudes", "magnitud","data"+ File.separator+"magnitudes_aire.csv", "data"+ File.separator+"magnitudes_aire.xml");
		CSVReader csvMagnitudesTemperature = new CSVReader( "magnitudes", "magnitud","data"+ File.separator+"magnitudes_aire_meteo.csv", "data"+ File.separator+"magnitudes_aire_meteo.xml");
		csvPollution.loadCSVData();
		csvTemperature.loadCSVData();
		csvStations.loadCSVData();
		csvMagnitudesPollution.loadCSVData();
		csvMagnitudesTemperature.loadCSVData();

	}
}
