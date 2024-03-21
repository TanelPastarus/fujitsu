package com.fujitsu.delivery.repository;

import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.model.CityBaseFee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityBaseFeeRepository extends CrudRepository<CityBaseFee, Long> {
    CityBaseFee findCityBaseFeeByCity(City city);
}
