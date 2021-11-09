package ioutils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * class that helps jaxb to parse localdatetime objects
 * @author sps169, FedericoTB
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    /**
     * tells jaxb how to unmarshal strings as localdatetimes
     * @param s string to unmarshal
     * @return localdatetime
     * @throws Exception
     */
    @Override
    public LocalDateTime unmarshal(String s) throws Exception {
        return LocalDateTime.parse(s);
    }

    /**
     * tells jaxb how to marshal localdatetimes as strings
     * @param localDateTime datetime to marshal
     * @return marshable string
     * @throws Exception if you miss
     */
    @Override
    public String marshal(LocalDateTime localDateTime) throws Exception {
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
