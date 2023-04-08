package coffee.lkh.weathermonitoringv2.models.remote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "weather")
public class Weather{
    @Id
    @JsonIgnore
    private String id;
    @JsonProperty("icon")
    public String getIcon() {
        return this.icon; }
    public void setIcon(String icon) {
        this.icon = icon; }

    @Field("icon")
    String icon;
    @JsonProperty("code")
    public String getCode() {
        return this.code; }
    public void setCode(String code) {
        this.code = code; }
    @Field("code")
    String code;
    @JsonProperty("description")
    public String getDescription() {
        return this.description; }
    public void setDescription(String description) {
        this.description = description; }

    @Field("description")
    String description;
}
