package com.fujitsu.delivery.service;

import com.fujitsu.delivery.model.CityBaseFee;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryService {
    void addCityBaseFee(CityBaseFee cityBaseFee);
    double FindCityFee(String city, String vehicle);
    double findCityBaseFee(String city, String vehicle);
    void updateCityBaseFee(CityBaseFee updatedCity);
}
