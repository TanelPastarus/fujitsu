package com.fujitsu.delivery.service;

import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.constants.Vehicle;
import com.fujitsu.delivery.constants.WeatherPhenomenon;
import com.fujitsu.delivery.model.CityBaseFee;
import com.fujitsu.delivery.model.VehicleExtraFees;
import com.fujitsu.delivery.model.Weather;
import com.fujitsu.delivery.repository.CityBaseFeeRepository;
import com.fujitsu.delivery.repository.VehicleExtraFeeRepository;
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
public class DeliveryServiceTests {

    @Mock
    private CityBaseFeeRepository cityBaseFeeRepository;

    @Mock
    private VehicleExtraFeeRepository vehicleExtraFeeRepository;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    private CityBaseFee cityBaseFee;
    private VehicleExtraFees vehicleExtraFees;
    private Weather weather;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        deliveryService = new DeliveryServiceImpl(cityBaseFeeRepository, weatherService, vehicleExtraFeeRepository);
        cityBaseFee = new CityBaseFee(1L, City.TARTU, 1.0, 2.0, 3.0);
        weather = new Weather(1L, City.TARTU, 20.0, 10.0, WeatherPhenomenon.SNOWY, 123456, Timestamp.from(Instant.now()));
        vehicleExtraFees = new VehicleExtraFees(1L, Vehicle.CAR, 0.0, 0.0, 5.0, 2.0, 2.0, 0.0, 0.0);

    }

    @Test
    public void addCityBaseFee_callsRepositorySave() {
        deliveryService.addCityBaseFee(cityBaseFee);
        verify(cityBaseFeeRepository, times(1)).save(cityBaseFee);
    }

    @Test
    public void findCityBaseFee_returnsCorrectFee() {
        String city = "TARTU";
        String vehicle = "CAR";
        when(cityBaseFeeRepository.findCityBaseFeeByCity(City.valueOf(city))).thenReturn(cityBaseFee);
        double fee = deliveryService.findCityBaseFee(city, vehicle);
        assertEquals(3.0, fee);
    }

    @Test
    public void findCityFee_returnsCorrectFee() {
        String city = "TARTU";
        String vehicle = "CAR";

        when(cityBaseFeeRepository.findCityBaseFeeByCity(City.valueOf(city))).thenReturn(cityBaseFee);
        when(weatherService.findLatestWeatherByCity(City.valueOf(city))).thenReturn(weather);
        when(vehicleExtraFeeRepository.findVehicleExtraFeesByVehicle(Vehicle.valueOf(vehicle))).thenReturn(vehicleExtraFees);

        double fee = deliveryService.FindCityFee(city, vehicle);

        assertEquals(10.0, fee);

    }

}
