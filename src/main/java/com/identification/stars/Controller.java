package com.identification.stars;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
public class Controller {
    private final StarsCatalogue service;

    public Controller(StarsCatalogue service) {
        this.service = service;
    }

    @GetMapping("/explore")
    public List<Object[]> explore(@RequestParam double latitude, @RequestParam double longitude) {
        List<Object[]> starsData = service.getStarsToExplore(latitude, longitude);
        return starsData; //.subList(0, 100000);
    }
}
