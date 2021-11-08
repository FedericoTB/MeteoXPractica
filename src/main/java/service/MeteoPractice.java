package service;

import ioutils.*;
import org.jdom2.JDOMException;
import pojos.*;

import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * Facade implementation of the Analytics object creation
 * @author sps169, FedericoTB
 */
public class MeteoPractice {
    private static final String PARENT_PATH = "data"+ File.separator;
    private static final String STATIONS_FILE = PARENT_PATH+"calidad_aire_estaciones.xml";
    private static final String METEOROLOGY_FILE = PARENT_PATH+"calidad_aire_datos_meteo_mes.xml";
    private static final String CONTAMINATION_FILE = PARENT_PATH+"calidad_aire_datos_mes.xml";
    private static final String MAGNITUDES_FILE = PARENT_PATH+"magnitudes_aire.xml";
    private static final String MAGNITUDES_METEO_FILE = PARENT_PATH+"magnitudes_aire_meteo.xml";
    /**
     * Generates an Analytics object given a {@link String} city and directory URI.
     * @param city {@link String} name of city
     * @param directoryURI {@link String} URI of the directory where to output images and html
     * @return {@link Analytics} object containing list of temperature and contamination, the station and the HTML
     */
    public static Inform runMeteoInform(String city, String directoryURI) throws IOException, JDOMException, URISyntaxException {
        Path directory = DataReading.createDirectory(directoryURI);
        long initialTime = System.currentTimeMillis();
        if (directory != null) {
            CSVReader.generateXMLFilesFromCSV();
            Station station = DataReading.getStation(city, STATIONS_FILE).orElse(null);
            if (station != null) {
                //thread approach to reading csv and filtering streams
                ThreadPoolExecutor threads = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
                RunnableXMLStationReader stationRunnable = new RunnableXMLStationReader(city,STATIONS_FILE);
                RunnableXMLMeasuresReader contaminationRunnable = new RunnableXMLMeasuresReader(CONTAMINATION_FILE,station);
                RunnableXMLMeasuresReader meteorologyRunnable = new RunnableXMLMeasuresReader(METEOROLOGY_FILE,station);
                RunnableXMLMagnitudesReader magnitudeRunnable = new RunnableXMLMagnitudesReader(MAGNITUDES_FILE);
                RunnableXMLMagnitudesReader magnitudeMeteoRunnable = new RunnableXMLMagnitudesReader(MAGNITUDES_METEO_FILE);
                threads.execute(stationRunnable);
                threads.execute(contaminationRunnable);
                threads.execute(meteorologyRunnable);
                threads.execute(magnitudeRunnable);
                threads.execute(magnitudeMeteoRunnable);
                threads.shutdown();
                while (threads.getActiveCount()!= 0) {
                    try {
                        Thread.sleep(1);
                    }catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Inform inform = null;
                String dbPath = directoryURI+ File.separator + "db" + File.separator+"mediciones.xml";
                try {
                    if (Files.exists(Path.of(dbPath))) {
                        inform = JAXBController.getInstance().getDB(dbPath);
                    }else {
                        inform = new Inform();
                    }
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
                Analytics currentAnalytics = buildAnalytics(directory, initialTime, station,
                        contaminationRunnable.getList(), meteorologyRunnable.getList(),
                        magnitudeRunnable.getList(),  magnitudeMeteoRunnable.getList());
                currentAnalytics.htmlBuilder();
                try {
                    currentAnalytics.generateHtml();
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
                inform.getAnalyticsDB().add(currentAnalytics);

                try {
                    JAXBController jaxb = JAXBController.getInstance();
                    jaxb.setDataBase(inform);
                    //jaxb.printXML();
                    jaxb.writeXMLFile(directoryURI);
                } catch (IOException | JAXBException e) {
                    e.printStackTrace();
                }
                JDOM jdom = new JDOM (dbPath);
                jdom.loadData();
                try {
                    MarkDownGenerator.generateMarkdownOfCityMeans(jdom.getAnalyticsOfCity(city), directoryURI + File.separator + "informe-"+city+".md");
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("That city doesn't exists");
                return null;
            }
        }
        return null;
    }

    /**
     * Receives the lists with Measures and Magnitudes and creates the final lists used by {@link Analytics}
     * @param directory {@link Path} used to store the output data
     * @param initialTime long used to measure time of execution
     * @param station {@link Station} used to filter data
     * @param contaminationMeasuresList {@link List} of {@link Measure} of contamination data
     * @param meteorologyMeasuresList {@link List} of {@link Measure} of temperature data
     * @param magnitudes {@link List} of {@link Magnitude} of contamination magnitudes
     * @param magnitudesMeteo {@link List} of {@link Magnitude} of temperature magnitudes
     * @return {@link Analytics} Analytics object built
     */
    private static Analytics buildAnalytics(Path directory, long initialTime, Station station,
                                            List<Measure> contaminationMeasuresList, List<Measure> meteorologyMeasuresList,
                                            List<Magnitude> magnitudes, List<Magnitude> magnitudesMeteo) {
        List<MonthData> contaminationReport = new ArrayList<>();
        List<MonthData> meteorologyReport = new ArrayList<>();
        //fill the Monthly Data lists with filtered and parsed data of the params lists.
        for (Magnitude type : magnitudes) {
            contaminationReport.add(
                    new MonthData(
                            contaminationMeasuresList.stream()
                                .filter(s -> s.getMagnitude().equals(type.getCodMagnitude()))
                                .collect(Collectors.toList()),
                            type));
        }
        for (Magnitude type : magnitudesMeteo) {
            meteorologyReport.add(
                    new MonthData(
                            meteorologyMeasuresList.stream()
                                    .filter(s -> s.getMagnitude().equals(type.getCodMagnitude()))
                                    .collect(Collectors.toList()),
                            type));
        }

        Analytics generalReport = null;
        try {
            generalReport = new Analytics(contaminationReport, meteorologyReport, station, directory, initialTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generalReport;
    }
}
