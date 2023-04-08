package coffee.lkh.weathermonitoringv2.models;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Weatherforecast {

    public Date Date;

    public int TemperatureC;

    public int getTemperatureF() {
        return  32 + (int)(this.TemperatureC / 0.5556);
    }

    public int TemperatureF;

    public String Summary;
}
