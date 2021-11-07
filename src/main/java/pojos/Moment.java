package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor

/**
 * Class that models a measure and the instant it was measured at.
 * @author sps169, FedericoTB
 */
@XmlRootElement(name ="moment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Moment {
    private LocalDateTime date;
    private Float value;
}
