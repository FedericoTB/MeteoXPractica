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
        try {
            MeteoPractice.runMeteoInform(args[0], args[1]);
        } catch (JDOMException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
