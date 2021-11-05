package ioutils;

import org.jdom2.JDOMException;
import pojos.Station;

import java.io.IOException;

public class RunnableXMLStationReader extends RunnableXMLReader{
    private Station station;
    private final String city;

    /**
     * Creates a RunnableXMLMeasuresReader
     * @param file {@link String} file URI of readable data
     */
    public RunnableXMLStationReader(String city,String file) {
        super(file);
        this.city=city;
    }
    @Override
    public void run() {
        try {
            this.station =  DataReading.getStation(this.city,super.getFile()).orElse(null);
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
    }

    public Station getStation() {
        return this.station;
    }
}
