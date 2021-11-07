package ioutils;

import service.Analytics;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class JAXBController {
    private static JAXBController instance;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private Analytics analytics;

    private JAXBController(){}

    public static JAXBController getInstance(){
        if (instance == null){
            instance = new JAXBController();
        }
        return instance;
    }

    private void convertObjectToXML(Analytics analytics) throws JAXBException {
        this.analytics = analytics;
        JAXBContext context = JAXBContext.newInstance(Analytics.class);
        this.marshaller = context.createMarshaller();
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
    }

    public void setAnalytics(Analytics analytics) throws JAXBException {
        convertObjectToXML(analytics);
    }

    public void writeXMLFile(String uri) throws JAXBException, IOException {
        if(!Files.exists(Path.of(uri + File.separator + "db"))){Files.createDirectory(Path.of(uri+File.separator+"db"));}
        this.marshaller.setProperty(Marshaller.JAXB_ENCODING, "windows-1252");
        this.marshaller.marshal(analytics, new File(uri+File.separator+"db"+File.separator+"mediciones.xml"));
    }

    public void printXML() throws JAXBException {
        this.marshaller.marshal(analytics, System.out);
    }
    private Analytics convertXMLToObject(String uri) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Analytics.class);
        this.unmarshaller = context.createUnmarshaller();
        this.analytics = (Analytics) this.unmarshaller.unmarshal(new File(uri));
        return this.analytics;
    }

    public Analytics getAnalytics(String uri) throws JAXBException {
        return convertXMLToObject(uri);
    }
}
