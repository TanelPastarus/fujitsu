package com.fujitsu.delivery.model;

import com.fujitsu.delivery.constants.City;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityBaseFee {
    @Id
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private City city;

    @PositiveOrZero
    private double bikeFee;

    @PositiveOrZero
    private double scooterFee;

    @PositiveOrZero
    private double carFee;
}
