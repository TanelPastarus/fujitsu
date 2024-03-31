package com.fujitsu.delivery.model;

import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.constants.WeatherPhenomenon;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private City name;

    // Maximum and minimum highest temperature ever recorded
    @Max(value = 60)
    @Min(value = -90)
    @NotNull
    private Double airTemperature;

    // Maximum surface wind
    @PositiveOrZero
    @Max(value = 115)
    @NotNull
    private Double windSpeed;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WeatherPhenomenon weatherPhenomenon;

    @NotNull
    private Integer WMOCode;

    @CreationTimestamp
    private Timestamp timestamp;
}
