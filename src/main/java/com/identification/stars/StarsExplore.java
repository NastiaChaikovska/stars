package com.identification.stars;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StarsExplore {
    @JsonProperty("stars")
    List<Object[]> stars;

    @JsonProperty("center_star_id")
    Integer centerStarId;

    @JsonProperty("center_star_ra")
    Double centerStarRa;

    @JsonProperty("center_star_dec")
    Double centerStarDec;


    public StarsExplore(List<Object[]> stars, Integer centerStarId, Double ra, Double dec){
        this.stars = stars;
        this.centerStarId = centerStarId;
        this.centerStarRa = ra;
        this.centerStarDec = dec;
    }
}

