package com.fujitsu.delivery.repository;

import com.fujitsu.delivery.constants.Vehicle;
import com.fujitsu.delivery.model.VehicleExtraFees;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleExtraFeeRepository extends CrudRepository<VehicleExtraFees, Long> {
    VehicleExtraFees findVehicleExtraFeesByVehicle(Vehicle vehicle);
}
