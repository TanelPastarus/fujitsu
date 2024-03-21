package com.fujitsu.delivery.service;

import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.model.CityBaseFee;
import com.fujitsu.delivery.constants.Vehicle;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryService {
    void addCityBaseFee(CityBaseFee cityBaseFee);
    double FindCityFee(City city, Vehicle vehicle);
}
