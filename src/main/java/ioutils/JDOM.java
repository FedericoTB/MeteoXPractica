package ioutils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class JDOM {
    public Document document;
    private final String INPUT_URI;
    private final String ROOT_ELEMENT_NAME;
    private final String OUTPUT_URI;
    private List<String> headers_list;

    public JDOM (String output_uri, String rootElementName, String input_uri) {
        this.INPUT_URI = input_uri;
        this.ROOT_ELEMENT_NAME = rootElementName;
        this.OUTPUT_URI = output_uri;
    }
    public void generateDocument() {
        initDocument();
        try {
            fillDocumentDataFromCSV();
            writeFileFromDocument();
        } catch(IOException ex) {
            System.err.println("XML File could not be written from csv");
        }
    }

    public XMLOutputter getPrettyOutputter () {
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        return xmlOutput;
    }

    public void writeFileFromDocument() throws IOException {
        XMLOutputter xmlOutput = this.getPrettyOutputter();
        xmlOutput.output(this.document, Files.newBufferedWriter(Path.of(this.OUTPUT_URI)));
    }

    public void fillDocumentDataFromCSV() throws IOException {
            Stream<String> dataStream = Files.lines(Path.of(this.INPUT_URI));
            dataStream.forEach(s -> {
                if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
                    addLineElement(s);
                }else{
                    headers_list = Arrays.asList(s.split(";"));
                }
            });
    }
    public void addLineElement(String line) {
        Element measure = new Element("measure");
        Element root = document.getRootElement();
        List<String> splitted = Arrays.asList(line.split(";"));
        for(int i = 0; i < splitted.size(); i++)
            measure.addContent(new Element(headers_list.get(i)).setText(splitted.get(i)));
        root.addContent(measure);
    }

    public void initDocument() {
        this.document = new Document();
        this.document.setRootElement(new Element(ROOT_ELEMENT_NAME));
    }

    public static void main (String[] args) throws IOException {
        JDOM jdom = new JDOM("data//calidad_aire_datos_mes.xml", "temperatura", "data//calidad_aire_datos_mes.csv");
        jdom.generateDocument();
    }
}
