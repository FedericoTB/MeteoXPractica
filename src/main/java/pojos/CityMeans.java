package pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Class that models means of measures of all measures from the XML database for city.
 * @author sps169, FedericoTB
 */
public class CityMeans {
    private String city;
    private List<DataMean> contaminationMeans;
    private List<DataMean> meteorologyMeans;

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("##" + city + "\n");
        contaminationMeans.forEach(output::append);
        meteorologyMeans.forEach(output::append);
        return output.toString();
    }
}
