package ioutils;

import org.jdom2.Document;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import pojos.CityMeans;
import pojos.DataMean;
import service.Analytics;

import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JDOM{
    private Document document;
    private final String URI;

    public JDOM (String uri) {
        this.URI = uri;
    }

    public void loadData() throws IOException, JDOMException {
        initDocument();
        fillDocumentDataFromXML();
    }

    private void fillDocumentDataFromXML() throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();
        this.document = saxBuilder.build(new File(this.URI));
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

    public List<CityMeans> getAnalyticsOfCity (String city) throws XPathExpressionException {
        XPathFactory pathFactory = XPathFactory.instance();
        XPathExpression<Element> xpathQuery = pathFactory.compile("/database/informs/inform[station/stationCity = \""+city+"\"]", Filters.element());
        return xpathQuery.evaluate(this.document).stream().map(s -> {
            return new CityMeans(
                    city,
            s.getChild("contamination").getChildren("contaminationData").stream().map(t ->
                    new DataMean(
                            Double.parseDouble(t.getChildText("meanValueOfMeasures")) ,
                            t.getChild("type").getChildText("description")
                    )
            ).collect(Collectors.toList()),
            s.getChild("meteorology").getChildren("meteorologyData").stream().map(t ->
                    new DataMean(
                            Double.parseDouble(t.getChildText("meanValueOfMeasures")) ,
                            t.getChild("type").getChildText("description")
                    )
            ).collect(Collectors.toList()));
        }).collect(Collectors.toList());
    }

    public Document getDocument() {
        return document;
    }
}
