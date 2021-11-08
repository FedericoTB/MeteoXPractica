package pojos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import service.Analytics;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "database")
public class Inform {
    @XmlElementWrapper(name = "informs")
    private List<Analytics> inform = new ArrayList<>();

    public List<Analytics> getAnalyticsDB() {
        return this.inform;
    }
}
