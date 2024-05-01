package com.identification.stars;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {
    private final StarsCatalogue service;

    public Controller(StarsCatalogue service) {
        this.service = service;
    }

    @GetMapping("/explore")
    public StarsExplore explore(@RequestParam double latitude, @RequestParam double longitude) {
        StarsExplore starsData = service.getStarsToExplore(latitude, longitude);
        return starsData; //.subList(0, 100000);
    }

    @GetMapping("/star_info")
    @ResponseBody
    public StarData getInfo(@RequestParam String id) {
        return service.getStarInfo(id);
    }
}
