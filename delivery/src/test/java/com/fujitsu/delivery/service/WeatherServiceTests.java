package com.fujitsu.delivery.service;

import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.constants.WeatherPhenomenon;
import com.fujitsu.delivery.model.Weather;
import com.fujitsu.delivery.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class WeatherServiceTests {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    private Weather weather;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        weatherService = new WeatherServiceImpl(weatherRepository);
        weather = new Weather(1L, City.TARTU, 20.0, 10.0, WeatherPhenomenon.SNOWY, 123456, Timestamp.from(Instant.now()));
    }

    @Test
    public void testAddWeather() {
        weatherService.addWeather(weather);
        verify(weatherRepository, times(1)).save(weather);

    }

    @Test
    public void findLatestWeatherByCityReturnsCorrectWeather() {

        when(weatherRepository.findTop1WeatherByNameOrderByTimestampDesc(City.TARTU)).thenReturn(weather);

        Weather actualWeather = weatherService.findLatestWeatherByCity(City.TARTU);

        assertEquals(weather, actualWeather);
    }
}
