package com.fujitsu.delivery.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


@Component
public class WeatherDataImport {
    private final WeatherService weatherService;

    public WeatherDataImport(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Imports weather data from the Estonian Weather Service
     * Configurable in resources/application.properties file
     */
    @Scheduled(cron = "${weather.import.cron}")
    public void importWeatherData() throws IOException, ParserConfigurationException, SAXException {
        weatherService.updateWeatherData();
    }
}
