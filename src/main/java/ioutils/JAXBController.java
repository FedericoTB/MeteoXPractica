package ioutils;


import pojos.Inform;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * class that works with an XSD and XML files to access data
 * @author sps169, FedericoTB
 */
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

    /**
     * creates marshaller to turn object to xml
     * @param db inform to parse
     * @throws JAXBException if jaxb fails to create marshaller
     */
    public void convertObjectToXML(Inform db) throws JAXBException {
        this.db = db;
        JAXBContext context = JAXBContext.newInstance(Inform.class);
        this.marshaller = context.createMarshaller();
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
        this.marshaller.setProperty(Marshaller.JAXB_ENCODING, "windows-1252");
    }

    /**
     * sets inform as database
     * @param db inform object used
     * @throws JAXBException
     */
    public void setDataBase(Inform db) throws JAXBException {
        convertObjectToXML(db);
    }

    /**
     * outputs xml file
     * @param uri path of xml
     * @throws JAXBException if jaxb fails to marshall
     * @throws IOException if directory creation fails
     */
    public void writeXMLFile(String uri) throws JAXBException, IOException {
        if(!Files.exists(Path.of(uri + File.separator + "db"))){
            Files.createDirectory(Path.of(uri+File.separator+"db"));
        }
        this.marshaller.marshal(db, new File(uri+File.separator+"db"+File.separator+"mediciones.xml"));

    }

    /**
     * outputs an objects as xml in console
     * @throws JAXBException if marshalling fails
     */
    public void printXML() throws JAXBException {
        this.marshaller.marshal(db, System.out);
    }

    /**
     * reads an xml and parses it to inform object
     * @param uri xml file path
     * @return inform object
     * @throws JAXBException if unmarshalling fails
     */
    public Inform convertXMLToObject(String uri) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Inform.class);
        this.unmarshaller = context.createUnmarshaller();
        this.db = (Inform) this.unmarshaller.unmarshal(new File(uri));
        return this.db;
    }

    /**
     * gets db from xml uri
     * @param uri xml file
     * @return Inform object
     * @throws JAXBException if unmarshalling fails
     */
    public Inform getDB(String uri) throws JAXBException {
        return convertXMLToObject(uri);
    }
}
