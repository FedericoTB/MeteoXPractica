package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Class that models a measurement.
 * @author sps169, FedericoTB
 */
@XmlRootElement(name ="hour_measurement")
@XmlAccessorType(XmlAccessType.FIELD)
public class HourMeasurement {

    /**
     * int value that stores the hour of measurement
     */
    private int hour;

    /**
     * value of the measurement
     */
    private float value;

    /**
     * validation of the measurement. It can be {"V", "N", "T"}.
     */
    private char validation;
}
