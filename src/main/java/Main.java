import ioutils.JAXBController;
import org.jdom2.JDOMException;
import pojos.Inform;
import service.MeteoPractice;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main (String[] args) throws IOException {
        Inform inform = null;
        try {
            inform = MeteoPractice.runMeteoInform(args[0], args[1]);
        } catch (JDOMException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        if (inform != null) {
            try {
                JAXBController jaxb = JAXBController.getInstance();
                jaxb.setDataBase(inform);
                jaxb.printXML();
                jaxb.writeXMLFile(args[1]);
                jaxb.getDB(args[1]+ File.separator + "db" + File.separator+"mediciones.xml").getAnalyticsDB().stream().forEach(System.out::println);
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
            }
        }
    }
}
