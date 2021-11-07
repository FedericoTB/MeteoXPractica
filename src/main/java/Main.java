import ioutils.CSVReader;
import ioutils.JAXBController;
import ioutils.JDOM;
import org.jdom2.JDOMException;
import service.Analytics;
import service.MeteoPractice;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main (String[] args) throws IOException {
        Analytics analysis = null;
        try {
            analysis = MeteoPractice.generateMeteoAnalysis(args[0], args[1]);
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        if (analysis != null) {
            analysis.htmlBuilder();
            try {
                analysis.generateHtml();
                JAXBController jaxb = JAXBController.getInstance();
                jaxb.setAnalytics(analysis);
                jaxb.printXML();
                jaxb.writeXMLFile(args[1]);
            } catch (IOException | URISyntaxException | JAXBException e) {
                e.printStackTrace();
            }
        }
    }
}
