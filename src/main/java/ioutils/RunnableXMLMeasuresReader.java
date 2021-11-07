package ioutils;

import org.jdom2.JDOMException;
import pojos.Measure;
import pojos.Station;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Runnable class that reads and parses a magnitude document with a given charset and stores it in a list of Magnitudes
 * @author sps169, FedericoTB
 */
public class RunnableXMLMeasuresReader extends RunnableXMLReader{
    private List<Measure> list;
    private Station station;
    /**
     * Creates a RunnableXMLMeasuresReader
     * @param file {@link String} file URI of readable data
     */
    public RunnableXMLMeasuresReader(String file, Station station) {
        super(file);
        this.list = new ArrayList<Measure>();
        this.station = station;
    }
    @Override
    public void run() {
        try {
            this.list = DataReading.getMeasures(DataReading.getMeasuresDataOfStation(station,super.getFile())) ;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    public List<Measure> getList() {
        return this.list;
    }
}
