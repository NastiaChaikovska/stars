package com.identification.stars;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class Controller {
    private final StarsCatalogue service;

    public Controller(StarsCatalogue service) {
        this.service = service;
    }

    @GetMapping("/explore")
    public List<Object[]> explore() {
        List<Object[]> starsData = service.getStarsToExplore();
        return starsData;
    }

    @GetMapping("/central_star2")
    public CentralStar getCentralStar2(@RequestParam double latitude, @RequestParam double longitude) {
        return service.getCentralStar2(latitude, longitude);
    }

    @GetMapping("/central_star")
    public Integer getCentralStar(@RequestParam double latitude, @RequestParam double longitude) {
        return service.getCentralStar(latitude, longitude);
    }

    @GetMapping("/star_info")
    @ResponseBody
    public StarData getInfo(@RequestParam String id) {
        return service.getStarInfo(id);
    }
}
