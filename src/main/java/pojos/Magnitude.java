package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Class that models a magnitude
 * @author sps169, FedericoTB
 */
@XmlRootElement(name = "magnitude")
@XmlAccessorType(XmlAccessType.FIELD)
public class Magnitude {
    /**
     * Stores a numeric code representing the magnitude
     */
    private String codMagnitude;

    /**
     * Description of the magnitude
     */
    private String description;

    /**
     * Unit of the magnitude
     */
    private String unit;
}
