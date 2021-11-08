package ioutils;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.w3c.dom.NodeList;
import pojos.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class that provides file reading utility.
 * @author sps169, FedericoTB
 */
public class DataReading {
    public static final String SEPARATOR = FileSystems.getDefault().getSeparator();
    /**
     * Default input data directory.
     */
    public static final String DATA_DIR = System.getProperty("user.dir") + SEPARATOR + "data";

    /**
     * Method that receives the String path where the file is located
     * and returns a {@link JDOM}
     * containing its lines.
     * @param path {@link String} that contains the URI of the file.
     * @return {@link Stream} of {@link String} if the file is readable, null Stream otherwise.
     */
    public static JDOM getFile(String path) throws IOException, JDOMException {
        JDOM jdom = new JDOM(path);
        jdom.loadData();
        return jdom;
    }

    /**
     * Method that deletes a directory and EVERYTHING INSIDE OF IT.
     * @param path {@link Path} to be erased.
     * @throws IOException if path can't be accessed.
     */
    private static void deleteDirectory(Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> pathStream = Files.newDirectoryStream(path)) {
                for (Path killable : pathStream) {
                    deleteDirectory(killable);
                }
            }
        }
        Files.delete(path);
    }

    /**
     * Creates a directory given a {@link String} URI.
     * If the directory exists, it asks the user if the directory should be erased.
     * The method returns null if the user doesn't want to erase the directory.
     * @param uri {@link String} containing the URI.
     * @return {@link Path} of the new directory if operation was successful, null otherwise.
     */
    public static Path createDirectory(String uri) {
        Path directoryPath = null;
        if (!Files.exists(Path.of(uri))) {
            try {
                directoryPath = Files.createDirectory(Path.of(uri));
            }catch (IOException e) {
                System.err.println("Couldn't create output directory");
            }
        }else {
            directoryPath = Path.of(uri);
        }
        return directoryPath;
    }

    /**
     * Gets a {@link Station} from a csv given a {@link String} containing its name.
     * File is read using the "windows-1252" charset.
     * @param city {@link String} containing the station name.
     * @param stationFile {@link String} URI of the csv file where station data is stored.
     * @return {@link Optional} of {@link Station}.
     */
    public static Optional<Station> getStation(String city, String stationFile) throws IOException, JDOMException {
        JDOM fileData = getFile(stationFile);
        Optional<Station> estacion = Optional.empty();
         if(fileData!=null){
            List<Element> estacionesXml = fileData.getDocument().getRootElement().getChildren("estacion");
            Optional<Element> estacionElement =  estacionesXml.stream().filter(e->e.getChildren("estacion_municipio").stream().findFirst().get().getText().equals(city)).findFirst();
            if (estacionElement.isPresent())
            {
                estacion = Optional.of(new Station(estacionElement.get().getChildText("estacion_codigo"),
                        estacionElement.get().getChildText("zona_calidad_aire_descripcion"),
                        estacionElement.get().getChildText("estacion_municipio")));
            }
         }
        return estacion;
    }

    /**
     * Method that reads from a data xml file filtering its lines by station code.
     * @param station {@link Station} whose data we want to obtain.
     * @param file {@link String} containing the URI of the csv data file.
     * @return {@link Stream} of {@link String} containing the lines filtered by station.
     */
    public static Stream<Element> getMeasuresDataOfStation(Station station, String file) throws IOException, JDOMException {
        JDOM data = getFile(file);
        return  data.getDocument().getRootElement().getChildren("medicion")
                .stream().filter(m->m.getChildText("punto_muestreo").contains(station.getStationCode()));
    }

    /**
     * Method that parses a {@link Stream} of {@link String} containing data lines into
     * a {@link List} of {@link Measure}. If the stream is empty, the list is returned empty.
     * @param dataOfCity {@link Stream} of station data.
     * @return {@link List} of {@link Measure}
     */
    public static List<Measure> getMeasures(Stream<Element> dataOfCity) {
       return dataOfCity.map(e-> {
           List<HourMeasurement> listHours = new ArrayList<>();
           for (int i = 1; i<= 24;i++){
               HourMeasurement hourM = new HourMeasurement();
               hourM.setHour(i);

               if(i<10){
                   if(e.getChildText("v0"+i).equals("V")){
                       String value = e.getChildText("h0"+i).replace(",",".");
                       hourM.setValue(Float.valueOf(value));}
                   hourM.setValidation(e.getChildText("v0"+i).charAt(0));
               }else {
                   if(e.getChildText("v"+i).equals("V")){
                       String value = e.getChildText("h"+i).replace(",",".");
                   hourM.setValue(Float.valueOf(value));}
                   hourM.setValidation(e.getChildText("v"+i).charAt(0));
               }
                listHours.add(hourM);
           }
         return new Measure(
                  e.getChildText("magnitud"),
                  LocalDate.of(Integer.parseInt(e.getChildText("ano")),
                          Integer.parseInt(e.getChildText("mes")),
                          Integer.parseInt(e.getChildText("dia"))
                  ),listHours);
       }).collect(Collectors.toList());
    }
    public static List<Magnitude> getMagnitudes(String file) throws IOException, JDOMException {
        JDOM jdom = getFile(file);
        List<Element> magnitudesXml = jdom.getDocument().getRootElement().getChildren("magnitud");
        return magnitudesXml.stream().map(b-> new Magnitude( b.getChildText("codigo_magnitud"),
                b.getChildText("descripcion_magnitud"),
                b.getChildText("unidad"))).collect(Collectors.toList());
    }
}
