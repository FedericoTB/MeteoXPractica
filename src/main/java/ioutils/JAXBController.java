package ioutils;


import pojos.Inform;
import service.Analytics;

import javax.lang.model.element.Element;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class JAXBController {
    private static JAXBController instance;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private Inform db;

    private JAXBController(){}

    public static JAXBController getInstance(){
        if (instance == null){
            instance = new JAXBController();
        }
        return instance;
    }

    public void convertObjectToXML(Inform db) throws JAXBException {
        this.db = db;
        JAXBContext context = JAXBContext.newInstance(Inform.class);
        this.marshaller = context.createMarshaller();
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
        this.marshaller.setProperty(Marshaller.JAXB_ENCODING, "windows-1252");
    }

    public void setDataBase(Inform db) throws JAXBException {
        convertObjectToXML(db);
    }

    public void writeXMLFile(String uri) throws JAXBException, IOException {
        if(!Files.exists(Path.of(uri + File.separator + "db"))){
            Files.createDirectory(Path.of(uri+File.separator+"db"));
        }
        this.marshaller.marshal(db, new File(uri+File.separator+"db"+File.separator+"mediciones.xml"));

    }

    public void printXML() throws JAXBException {
        this.marshaller.marshal(db, System.out);
    }
    public Inform convertXMLToObject(String uri) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Inform.class);
        this.unmarshaller = context.createUnmarshaller();
        this.db = (Inform) this.unmarshaller.unmarshal(new File(uri));
        return this.db;
    }

    public Inform getDB(String uri) throws JAXBException {
        return convertXMLToObject(uri);
    }
}
