package com.fujitsu.delivery.service;

import com.fujitsu.delivery.BadWeatherException;
import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.constants.Vehicle;
import com.fujitsu.delivery.constants.WeatherPhenomenon;
import com.fujitsu.delivery.model.*;
import com.fujitsu.delivery.repository.CityBaseFeeRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final CityBaseFeeRepository cityBaseFeeRepo;
    private final WeatherService weatherService;

    public DeliveryServiceImpl(CityBaseFeeRepository cityBaseFeeRepo, WeatherService weatherService) {
        this.cityBaseFeeRepo = cityBaseFeeRepo;
        this.weatherService = weatherService;
    }

    @Override
    public void addCityBaseFee(CityBaseFee cityBaseFee) {
        cityBaseFeeRepo.save(cityBaseFee);
    }

    @Override
    public double FindCityFee(City city, Vehicle vehicle) {
        CityBaseFee cityBaseFee = cityBaseFeeRepo.findCityBaseFeeByCity(city);
        Weather weather = weatherService.findLatestWeatherByCity(city);
        double fee = 0;

        double airTemperature = weather.getAirTemperature();
        double windSpeed = weather.getWindSpeed();
        WeatherPhenomenon weatherPhenomenon = weather.getWeatherPhenomenon();

        if (vehicle == Vehicle.CAR) {
            return cityBaseFee.getCarFee();
        }

        if (vehicle == Vehicle.BIKE || vehicle == Vehicle.SCOOTER) {
            if (vehicle == Vehicle.BIKE) {
                fee = cityBaseFee.getBikeFee();

                if (windSpeed > 20) {
                    throw new BadWeatherException("Usage of selected vehicle type is forbidden");
                } else if (windSpeed >= 10) {
                    fee += 0.5;
                }

            }

            else {
                fee = cityBaseFee.getScooterFee();
            }

            if (airTemperature < -10) {
                fee += 1;
            } else if (airTemperature <= 0) {
                fee += 0.5;
            }

            if (weatherPhenomenon == WeatherPhenomenon.NONE) {
                return fee;
            }
            else if (weatherPhenomenon == WeatherPhenomenon.SNOWY) {
                fee += 1;
            }

            else if (weatherPhenomenon == WeatherPhenomenon.RAINY) {
                fee += 0.5;
            }

            else if (weatherPhenomenon == WeatherPhenomenon.GLAZE) {
                throw new BadWeatherException("Usage of selected vehicle type is forbidden");
            }
        }

        return fee;
    }
}
