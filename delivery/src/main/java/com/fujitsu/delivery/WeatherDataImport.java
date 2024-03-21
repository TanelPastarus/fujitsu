package com.fujitsu.delivery;
import com.fujitsu.delivery.service.WeatherService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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

    @Scheduled(cron = "${weather.import.cron}")
    public void importWeatherData() throws IOException, ParserConfigurationException, SAXException {
        // This method will be executed according to the configured cron expression
        weatherService.updateWeatherData();
    }
}
