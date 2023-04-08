package com.example.weathermonitoringv2.models.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Weather{
    @JsonProperty("icon")
    public String getIcon() {
        return this.icon; }
    public void setIcon(String icon) {
        this.icon = icon; }
    String icon;
    @JsonProperty("code")
    public String getCode() {
        return this.code; }
    public void setCode(String code) {
        this.code = code; }
    String code;
    @JsonProperty("description")
    public String getDescription() {
        return this.description; }
    public void setDescription(String description) {
        this.description = description; }
    String description;
}
