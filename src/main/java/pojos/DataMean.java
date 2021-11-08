package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataMean {
    private double mean;
    private String dataDescription;

    public String toString() {
        return dataDescription + " : " + mean + "\\\n";
    }
}
