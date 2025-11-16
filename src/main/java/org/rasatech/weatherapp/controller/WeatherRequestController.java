package org.rasatech.weatherapp.controller;

import lombok.AllArgsConstructor;
import org.rasatech.weatherapp.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/weather-app")
@AllArgsConstructor
public class WeatherRequestController {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherRequestController.class);

    private final WeatherService weatherService;

    @GetMapping(value = "/weather/{city}")
    public ResponseEntity<?> getWeather(@PathVariable String city) {
        LOG.info("Fetching weather data for city {} ", city);
        try {
            Map<String, Object> weather = weatherService.getWeather(city);
            return ResponseEntity.status(HttpStatus.OK).body(weather);
        } catch (Exception e) {
            LOG.error("Error while fetching data for city {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage());
        }
    }
}
