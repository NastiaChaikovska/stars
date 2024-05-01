package com.identification.stars;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StarsExplore {
    @JsonProperty("stars")
    List<Object[]> stars;

    @JsonProperty("center_star_id")
    Integer centerStarId;

    public StarsExplore(List<Object[]> stars, Integer centerStarId){
        this.stars = stars;
        this.centerStarId = centerStarId;
    }
}

