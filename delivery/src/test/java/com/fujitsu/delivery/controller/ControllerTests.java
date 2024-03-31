package com.fujitsu.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.constants.Vehicle;
import com.fujitsu.delivery.model.CityBaseFee;
import com.fujitsu.delivery.model.VehicleExtraFees;
import com.fujitsu.delivery.service.DeliveryService;
import com.fujitsu.delivery.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = Controller.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryService deliveryService;

    @MockBean
    private WeatherService weatherService;

    private ObjectMapper objectMapper;

    private VehicleExtraFees vehicleExtraFees;

    private CityBaseFee cityBaseFee;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        cityBaseFee = new CityBaseFee(1L, City.TARTU, 2.0, 3.0, 4.0);
        vehicleExtraFees = new VehicleExtraFees(1L, Vehicle.CAR, 1.0, 1.0, 6.0, 3.0, 3.0, 1.0, 1.0);
    }


    @Test
    public void getDeliveryFee_returnsCorrectFee_returnsCorrectHttpValue() throws Exception {
        String city = "TARTU";
        String vehicle = "CAR";
        double expectedFee = 10.0;

        Mockito.when(deliveryService.FindCityFee(city, vehicle)).thenReturn(expectedFee);

        mockMvc.perform(MockMvcRequestBuilders.get("/endfee/{city}/{vehicle}", city, vehicle)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(Double.toString(expectedFee)));

    }

    @Test
    public void getCityBaseFee_returnsCorrectFee_returnsCorrectHttpValue() throws Exception {
        String city = "TARTU";
        String vehicle = "CAR";
        double expectedBaseFee = 10.0;

        Mockito.when(deliveryService.findCityBaseFee(city, vehicle)).thenReturn(expectedBaseFee);

        mockMvc.perform(MockMvcRequestBuilders.get("/basefee/{city}/{vehicle}", city, vehicle)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(Double.toString(expectedBaseFee)));
    }

    @Test
    public void updateCityBaseFee_callsServiceUpdate_returnsCorrectHttpValue() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/basefee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityBaseFee)))
                .andExpect(status().isOk());

        verify(deliveryService, times(1)).updateCityBaseFee(cityBaseFee);
    }

    @Test
    public void updateVehicleExtraFee_callsServiceUpdate_returnsCorrectHttpValue() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/extrafee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleExtraFees)))
                .andExpect(status().isOk());

        verify(deliveryService, times(1)).updateVehicleExtraFees(vehicleExtraFees);
    }
}