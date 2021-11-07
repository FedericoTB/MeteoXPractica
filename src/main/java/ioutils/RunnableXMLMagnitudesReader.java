package ioutils;

import org.jdom2.JDOMException;
import pojos.Magnitude;
import pojos.Measure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunnableXMLMagnitudesReader extends RunnableXMLReader{
    private List<Magnitude> list;

    /**
     * Creates a RunnableXMLMeasuresReader
     * @param file {@link String} file URI of readable data
     */
    public RunnableXMLMagnitudesReader(String file) {
        super(file);
    }
    @Override
    public void run() {
        this.list = new ArrayList<Magnitude>();
        try {
            this.list =  DataReading.getMagnitudes(super.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    public List<Magnitude> getList() {
        return this.list;
    }
}
