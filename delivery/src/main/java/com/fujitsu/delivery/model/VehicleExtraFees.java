package com.fujitsu.delivery.model;

import com.fujitsu.delivery.constants.Vehicle;
import com.fujitsu.delivery.constraints.ZeroPositiveOrMinusOne;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleExtraFees {
    @Id
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Vehicle vehicle;

    @NotNull
    @ZeroPositiveOrMinusOne
    private Double atefUnderMinus10;

    @NotNull
    @ZeroPositiveOrMinusOne
    private Double atefBetweenMinus10And0;

    @NotNull
    @ZeroPositiveOrMinusOne
    private Double wsefBetween10And20;

    @NotNull
    @ZeroPositiveOrMinusOne
    private Double wsefOver20;

    @NotNull
    @ZeroPositiveOrMinusOne
    private Double wpefSnowy;

    @NotNull
    @ZeroPositiveOrMinusOne
    private Double wpefRainy;

    @NotNull
    @ZeroPositiveOrMinusOne
    private Double wpefGlaze;
}
