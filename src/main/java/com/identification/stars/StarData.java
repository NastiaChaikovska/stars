package com.identification.stars;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class StarData {
    @JsonProperty("mag")
    double mag;

    @JsonProperty("ra")
    double ra;

    @JsonProperty("dec")
    double dec;

    public StarData(double mag, double ra, double dec){
        this.mag = mag;
        this.ra = ra;
        this.dec = dec;
    }
}

