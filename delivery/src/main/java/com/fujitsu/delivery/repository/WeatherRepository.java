package com.fujitsu.delivery.repository;

import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.model.Weather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
    Weather findTop1WeatherByNameOrderByTimestampDesc(City city);
}
