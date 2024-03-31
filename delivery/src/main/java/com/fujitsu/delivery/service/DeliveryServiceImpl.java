package com.fujitsu.delivery.service;

import com.fujitsu.delivery.exception.BadWeatherException;
import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.constants.Vehicle;
import com.fujitsu.delivery.constants.WeatherPhenomenon;
import com.fujitsu.delivery.model.*;
import com.fujitsu.delivery.repository.CityBaseFeeRepository;
import com.fujitsu.delivery.repository.VehicleExtraFeeRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final CityBaseFeeRepository cityBaseFeeRepo;
    private final WeatherService weatherService;
    private final VehicleExtraFeeRepository vehicleRepo;

    public DeliveryServiceImpl(CityBaseFeeRepository cityBaseFeeRepo, WeatherService weatherService, VehicleExtraFeeRepository vehicleRepo) {
        this.cityBaseFeeRepo = cityBaseFeeRepo;
        this.weatherService = weatherService;
        this.vehicleRepo = vehicleRepo;
    }

    @Override
    public void addCityBaseFee(CityBaseFee cityBaseFee) {
        cityBaseFeeRepo.save(cityBaseFee);
    }

    /**
     * Find the base fee for a city and vehicle
     *
     * @param city    the city
     * @param vehicle the vehicle
     * @return the base fee
     */

    @Override
    public double findCityBaseFee(String city, String vehicle) {
        City c = City.valueOf(city.toUpperCase());
        Vehicle v = Vehicle.valueOf(vehicle.toUpperCase());
        CityBaseFee cityBase = cityBaseFeeRepo.findCityBaseFeeByCity(c);

        if (v == Vehicle.CAR) return cityBase.getCarFee();
        else if (v == Vehicle.BIKE) return cityBase.getBikeFee();
        else return cityBase.getScooterFee();
    }

    /**
     * Update the base fee for a city
     *
     * @param updatedCity the updated city
     */

    @Override
    public void updateCityBaseFee(CityBaseFee updatedCity) {
        City c = updatedCity.getCity();

        CityBaseFee cityBase = cityBaseFeeRepo.findCityBaseFeeByCity(c);

        cityBase.setCarFee(updatedCity.getCarFee());
        cityBase.setBikeFee(updatedCity.getBikeFee());
        cityBase.setScooterFee(updatedCity.getScooterFee());

        cityBaseFeeRepo.save(cityBase);
    }

    /**
     * Update the extra fees for a vehicle
     *
     * @param updatedVehicleExtraFees the updated vehicle extra fees
     */
    @Override
    public void updateVehicleExtraFees(VehicleExtraFees updatedVehicleExtraFees) {
        Vehicle v = updatedVehicleExtraFees.getVehicle();

        VehicleExtraFees vehicleOld = vehicleRepo.findVehicleExtraFeesByVehicle(v);

        vehicleOld.setWpefRainy(updatedVehicleExtraFees.getWpefRainy());
        vehicleOld.setWpefSnowy(updatedVehicleExtraFees.getWpefSnowy());
        vehicleOld.setWpefGlaze(updatedVehicleExtraFees.getWpefGlaze());
        vehicleOld.setWsefOver20(updatedVehicleExtraFees.getWsefOver20());
        vehicleOld.setWsefBetween10And20(updatedVehicleExtraFees.getWsefBetween10And20());
        vehicleOld.setAtefUnderMinus10(updatedVehicleExtraFees.getAtefUnderMinus10());
        vehicleOld.setAtefBetweenMinus10And0(updatedVehicleExtraFees.getAtefBetweenMinus10And0());

        vehicleRepo.save(vehicleOld);

    }


    /**
     * Find the delivery fee for a city and vehicle
     * If any of the values are -1, then the usage of the vehicle is forbidden due to bad weather
     *
     * @param city    the city
     * @param vehicle the vehicle
     * @return the delivery fee
     * @throws BadWeatherException if usage of the vehicle is forbidden due to bad weather
     */
    @Override
    public double FindCityFee(String city, String vehicle) {

        City c = City.valueOf(city.toUpperCase());
        Vehicle v = Vehicle.valueOf(vehicle.toUpperCase());

        CityBaseFee cityBaseFee = cityBaseFeeRepo.findCityBaseFeeByCity(c);
        Weather weather = weatherService.findLatestWeatherByCity(c);
        VehicleExtraFees vehicleExtraFees = vehicleRepo.findVehicleExtraFeesByVehicle(v);

        double fee = 0;
        Double WSEF = 0.0;
        Double ATEF = 0.0;
        Double WPEF = 0.0;

        double airTemperature = weather.getAirTemperature();
        double windSpeed = weather.getWindSpeed();
        WeatherPhenomenon weatherPhenomenon = weather.getWeatherPhenomenon();

        if (v == Vehicle.CAR) fee += cityBaseFee.getCarFee();
        else if (v == Vehicle.BIKE) fee += cityBaseFee.getBikeFee();
        else if (v == Vehicle.SCOOTER) fee += cityBaseFee.getScooterFee();

        if (windSpeed > 20) {
            WSEF = vehicleExtraFees.getWsefOver20();

        } else if (windSpeed >= 10) {
            WSEF = vehicleExtraFees.getWsefBetween10And20();

        }

        if (airTemperature < -10) {
            ATEF = vehicleExtraFees.getAtefUnderMinus10();
        } else if (airTemperature <= 0) {
            ATEF = vehicleExtraFees.getAtefBetweenMinus10And0();
        }

        if (weatherPhenomenon == WeatherPhenomenon.NONE) {
            WPEF = 0.0;
        } else if (weatherPhenomenon == WeatherPhenomenon.SNOWY) {
            WPEF = vehicleExtraFees.getWpefSnowy();
        } else if (weatherPhenomenon == WeatherPhenomenon.RAINY) {
            WPEF = vehicleExtraFees.getWpefRainy();
        } else if (weatherPhenomenon == WeatherPhenomenon.GLAZE) {
            WPEF = vehicleExtraFees.getWpefGlaze();
        }

        if (WPEF == -1 || WSEF == -1 || ATEF == -1) throw new BadWeatherException();

        fee += WPEF + WSEF + ATEF;

        return fee;
    }

}
