package com.fujitsu.delivery.controller;

import com.fujitsu.delivery.BadWeatherException;
import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.constants.Vehicle;
import com.fujitsu.delivery.constants.WeatherPhenomenon;
import com.fujitsu.delivery.model.*;
import com.fujitsu.delivery.service.DeliveryService;

import com.fujitsu.delivery.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@RestController
@RequestMapping("/")
public class Controller {
    private final DeliveryService deliveryService;
    private final WeatherService weatherService;


    public Controller(DeliveryService deliveryService, WeatherService weatherService) throws IOException, ParserConfigurationException, SAXException {
        this.deliveryService = deliveryService;
        this.weatherService = weatherService;
        weatherService.addWeather(new Weather(1L, City.TARTU, -11.0, 21.0, WeatherPhenomenon.RAINY, 26038, Timestamp.from(Instant.now()) ));
        weatherService.addWeather(new Weather(2L, City.PÄRNU, -5.0, 15.0, WeatherPhenomenon.GLAZE, 26038, Timestamp.from(Instant.now()) ));
        weatherService.addWeather(new Weather(3L, City.TALLINN, 10, 1, WeatherPhenomenon.SNOWY, 26038, Timestamp.from(Instant.now()) ));
        //weatherService.updateWeatherData();

    }

    @ExceptionHandler({BadWeatherException.class})
    public ResponseEntity<String> handleForbiddenVehicle() {
        return new ResponseEntity<>("Usage of selected vehicle type is forbidden", HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleVehicleOrCityNotFound() {
        return new ResponseEntity<>("No city or vehicle found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/")
    public ResponseEntity<String> defaultPage() {
        return new ResponseEntity<>("""
                Delivery API:  /city/vehicle <br>
                Update a city: /basefee/update <br>
                Get base fee for a city and vehicle: /basefee/city/vehicle <br>
                Supported cities: TARTU, TALLINN, PÄRNU <br>
                Supported vehicles: Car, Bike, Scooter
                """, HttpStatus.OK);

    }

    @GetMapping("/{city}/{vehicle}")
    public ResponseEntity<Double> getDeliveryFee(@PathVariable String city, @PathVariable String vehicle) {
            double fee = deliveryService.FindCityFee(city, vehicle);
            return new ResponseEntity<>(fee, HttpStatus.OK);
    }

    @GetMapping("/basefee/{city}/{vehicle}")
    public ResponseEntity<Double> getCityBaseFee(@PathVariable String city,  @PathVariable String vehicle) {
        double baseFee = deliveryService.findCityBaseFee(city, vehicle);
        return new ResponseEntity<>(baseFee, HttpStatus.OK);

    }

    @PutMapping("/basefee/update")
    public ResponseEntity<String> updateCityBaseFee(@Valid @RequestBody CityBaseFee city) {
        deliveryService.updateCityBaseFee(city);
        return new ResponseEntity<>("Basefees for city "+city.getCity()+" updated succesfully", HttpStatus.OK);
    }
}
