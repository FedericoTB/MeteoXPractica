package ioutils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import pojos.Measure;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class JDOM{
    private Document document;
    private final String URI;
    private List<Measure> measureList;

    public JDOM (String uri) {
        this.URI = uri;
    }

    public void loadData() throws IOException, JDOMException {
        initDocument();
        fillDocumentDataFromXML();
    }

    private void fillDocumentDataFromXML() throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();
        this.document = saxBuilder.build(this.URI);
    }

    private XMLOutputter getPrettyOutputter () {
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat().setEncoding("windows-1252"));
        return xmlOutput;
    }

    public void writeXMLFile(String outputUri) throws IOException {
        XMLOutputter xmlOutput = this.getPrettyOutputter();
        xmlOutput.output(this.document, Files.newBufferedWriter(Path.of(outputUri), Charset.forName("windows-1252")));
    }

    public void printXMLFile() throws IOException {
        XMLOutputter xmlOutput = this.getPrettyOutputter();
        xmlOutput.output(this.document, System.out);
    }
    public void initDocument() {
        this.document = new Document();
        this.document.setProperty("Charset", "windows-1252");
    }

    public Document getDocument() {
        return document;
    }
}
