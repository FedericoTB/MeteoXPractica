import ioutils.CSVReader;
import ioutils.JDOM;
import service.Analytics;
import service.MeteoPractice;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main (String[] args) throws IOException {
        CSVReader.generateXMLFilesFromCSV();
        JDOM leerTest = new JDOM("data/magnitudes_aire_meteo.xml");
        try {
            leerTest.loadData();
        }catch (Exception e) {
            System.err.println("cagaste");
        }
        leerTest.printXMLFile();
    }
}
