package com.fujitsu.delivery.controller;

import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.constants.WeatherPhenomenon;
import com.fujitsu.delivery.model.CityBaseFee;
import com.fujitsu.delivery.model.VehicleExtraFees;
import com.fujitsu.delivery.model.Weather;
import com.fujitsu.delivery.service.DeliveryService;
import com.fujitsu.delivery.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;

@RestController
@RequestMapping("/")
@Validated
public class Controller {
    private final DeliveryService deliveryService;
    private final WeatherService weatherService;


    public Controller(DeliveryService deliveryService, WeatherService weatherService) {
        this.deliveryService = deliveryService;
        this.weatherService = weatherService;
        // Some starting data
        weatherService.addWeather(new Weather(1L, City.TARTU, -11.0, 21.0, WeatherPhenomenon.RAINY, 26242, Timestamp.from(Instant.now())));
        weatherService.addWeather(new Weather(2L, City.PÄRNU, -5.0, 15.0, WeatherPhenomenon.GLAZE, 41803, Timestamp.from(Instant.now())));
        weatherService.addWeather(new Weather(3L, City.TALLINN, 10.0, 1.0, WeatherPhenomenon.SNOWY, 26038, Timestamp.from(Instant.now())));
    }

    /**
     * Default page
     *
     * @return - default page
     */
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

    /**
     * Get the delivery fee for a city and vehicle
     *
     * @param city    - city name
     * @param vehicle - vehicle type
     * @return - delivery fee
     */
    @GetMapping("/endfee/{city}/{vehicle}")
    public ResponseEntity<Double> getDeliveryFee(@PathVariable String city, @PathVariable String vehicle) {
        double fee = deliveryService.FindCityFee(city, vehicle);
        return new ResponseEntity<>(fee, HttpStatus.OK);
    }

    /**
     * Get the base fee for a city and vehicle
     *
     * @param city    - city name
     * @param vehicle - vehicle type
     * @return - base fee
     */
    @GetMapping("/basefee/{city}/{vehicle}")
    public ResponseEntity<Double> getCityBaseFee(@PathVariable String city, @PathVariable String vehicle) {
        double baseFee = deliveryService.findCityBaseFee(city, vehicle);
        return new ResponseEntity<>(baseFee, HttpStatus.OK);

    }

    /**
     * Update the base fee for a city
     *
     * @param city - city name
     */
    @PutMapping("/basefee/update")
    public ResponseEntity<String> updateCityBaseFee(@Valid @RequestBody CityBaseFee city) {
        deliveryService.updateCityBaseFee(city);
        return new ResponseEntity<>("Basefees for city " + city.getCity() + " updated succesfully", HttpStatus.OK);
    }

    /**
     * Update the extra fee for a vehicle
     *
     * @param vehicle - city name
     */
    @PutMapping("/extrafee/update")
    public ResponseEntity<String> updateVehicleExtraFee(@Valid @RequestBody VehicleExtraFees vehicle) {
        deliveryService.updateVehicleExtraFees(vehicle);
        return new ResponseEntity<>("Extrafees for vehicle " + vehicle.getVehicle() + " updated succesfully", HttpStatus.OK);
    }
}
