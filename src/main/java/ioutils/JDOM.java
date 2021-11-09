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
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JDOM utils for reading and accessing XML files node by node
 */
public class JDOM{
    private Document document;
    private final String URI;

    public JDOM (String uri) {
        this.URI = uri;
    }

    /**
     * reads XML into document
     * @throws IOException if reading fails
     * @throws JDOMException if jdom fails
     */
    public void loadData() throws IOException, JDOMException {
        initDocument();
        fillDocumentDataFromXML();
    }

    /**
     * uses sax to fill document with xml file data
     * @throws IOException reading fails
     * @throws JDOMException jdom parsing fails
     */
    private void fillDocumentDataFromXML() throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();
        this.document = saxBuilder.build(new File(this.URI));
    }

    /**
     * generates xml output properties object
     * @return xmloutputter with output properties
     */
    private XMLOutputter getPrettyOutputter () {
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat().setEncoding("windows-1252"));
        return xmlOutput;
    }

    /**
     * generates xml file
     * @param outputUri path of file to be created
     * @throws IOException file creation failed
     */
    public void writeXMLFile(String outputUri) throws IOException {
        XMLOutputter xmlOutput = this.getPrettyOutputter();
        xmlOutput.output(this.document, Files.newBufferedWriter(Path.of(outputUri), Charset.forName("windows-1252")));
    }

    /**
     * prints document with formatter
     * @throws IOException if output fails
     */
    public void printXMLFile() throws IOException {
        XMLOutputter xmlOutput = this.getPrettyOutputter();
        xmlOutput.output(this.document, System.out);
    }

    /**
     * inits document with spanish charset
     */
    public void initDocument() {
        this.document = new Document();
        this.document.setProperty("Charset", "windows-1252");
    }

    /**
     * runs through database document to filter only a certain city's informs using xpath
     * and output a list of CityMeans
     * @param city city to filter
     * @return List of city means with data of means and description of each data
     * @throws XPathExpressionException xpath failed
     */
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
