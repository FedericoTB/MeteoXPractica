import ioutils.CSVReader;
import ioutils.JDOM;
import org.jdom2.JDOMException;
import service.Analytics;
import service.MeteoPractice;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main (String[] args)  {
        Analytics analysis = null;
        try {
            analysis = MeteoPractice.generateMeteoAnalysis(args[0], args[1]);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        if (analysis!= null) {
            analysis.htmlBuilder();
            try {
                analysis.generateHtml();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

        /*   CSVReader.generateXMLFilesFromCSV();
        JDOM leerTest = new JDOM("data/magnitudes_aire_meteo.xml");
        try {
            leerTest.loadData();
        }catch (Exception e) {
            System.err.println("cagaste");
        }
        leerTest.printXMLFile();*/
    }
}
