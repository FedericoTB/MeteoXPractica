package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@AllArgsConstructor

/**
 * Class that models a Station.
 * @author sps169, FedericoTB
 */
@XmlRootElement(name="station")
@XmlAccessorType(XmlAccessType.FIELD)
public class Station {
    /**
     * Stores the stationCode provided by Comunidad de Madrid.
     */
    private String stationCode;
    private String stationZone;
    private String stationCity;
}
