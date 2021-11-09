package ioutils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * class that helps jaxb to parse localdate objects
 * @author sps169, FedericoTB
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    /**
     * tells jaxb how to unmarshal strings as localdates
     * @param s string
     * @return localdate
     * @throws Exception if you miss
     */
    @Override
    public LocalDate unmarshal(String s) throws Exception {
        return LocalDate.parse(s);
    }

    /**
     * tells jaxb how to marshal localdates as strings
     * @param localDate date to marshal
     * @return marshable string
     * @throws Exception if you miss
     */
    @Override
    public String marshal(LocalDate localDate) throws Exception {
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/uuuu"));
    }
}
