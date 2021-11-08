package pojos;

import ioutils.LocalDateAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
/**
 * Class that models the data of a whole month.
 * @author sps169, FedericoTB
 */
@XmlRootElement(name = "monthdata")
@XmlAccessorType(XmlAccessType.FIELD)
public class MonthData {
    @XmlElementWrapper(name ="measures")
    private List<Measure> measures;
    private Magnitude type;
    private Moment maxMeasure;
    private Moment minMeasure;
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate startDayMeasure;
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate endDayMeasure;
    private double meanValueOfMeasures;

    /**
     * Full Constructor for {@link MonthData}. Generates max, min, mean and starting and ending dates
     * out of the list of measurements.
     * @param measures {@link List} of {@link Measure}
     * @param type {@link Magnitude} of the measurements.
     */
    public MonthData(List<Measure> measures, Magnitude type) {
        this.measures = measures;
        if (measures.size() != 0) {
            this.startDayMeasure = measures.stream().map(s->s.getDay()).min(LocalDate::compareTo).get();
            this.endDayMeasure = measures.stream().map(s->s.getDay()).max(LocalDate::compareTo).get();
            this.maxMeasure = measures.stream().max((s, t) -> Float.compare(s.getMaxValue().getValue(), t.getMaxValue().getValue())).get().getMaxValue();
            this.minMeasure = measures.stream().min((s, t) -> Float.compare(s.getMinValue().getValue(), t.getMinValue().getValue())).get().getMinValue();
            this.meanValueOfMeasures = measures.stream().collect(Collectors.averagingDouble(s -> s.getDayMean()));
        }
        this.type = type;
    }

    @Override
    public String toString() {
        return "MonthData{" +
                "type='" + type + '\'' +
                ", maxMeasure=" + maxMeasure +
                ", minMeasure=" + minMeasure +
                ", meanValueOfMeasures=" + meanValueOfMeasures +
                '}';
    }
}


