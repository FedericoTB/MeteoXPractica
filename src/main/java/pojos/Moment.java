package pojos;

import ioutils.LocalDateTimeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

/**
 * Class that models a measure and the instant it was measured at.
 * @author sps169, FedericoTB
 */
@XmlRootElement(name ="moment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Moment {
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime date;
    private Float value;
}
