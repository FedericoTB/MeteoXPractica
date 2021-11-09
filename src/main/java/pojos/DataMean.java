package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * Class that models means of a measures with description of the magnitude
 * @author sps169, FedericoTB
 */
public class DataMean {
    private double mean;
    private String dataDescription;

    public String toString() {
        return dataDescription + " : " + mean + "\\\n";
    }
}
