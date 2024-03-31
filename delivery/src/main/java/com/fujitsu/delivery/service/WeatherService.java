package com.fujitsu.delivery.service;

import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.model.Weather;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Service
public interface WeatherService {
    void updateWeatherData() throws IOException, SAXException, ParserConfigurationException;

    void addWeather(Weather weather);

    Weather findLatestWeatherByCity(City city);
}
