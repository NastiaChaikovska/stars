package com.identification.stars;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CentralStar {
    @JsonProperty("center_star_id")
    Integer centerStarId;

    @JsonProperty("center_star_ra")
    Double centerStarRa;

    @JsonProperty("center_star_dec")
    Double centerStarDec;

    @JsonProperty("center_star_mag")
    Double centerStarMag;
}
