package com.sensordata.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sensordata.demo.entities.Sensor;
import com.sensordata.demo.entities.SensorMeasurement;
import com.sensordata.demo.exceptions.*;
import com.sensordata.demo.model.SensorInput;
import com.sensordata.demo.services.SensorService;
import com.sensordata.demo.utils.DateUtil;
import com.sensordata.demo.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import static com.sensordata.demo.exceptions.ErrorCode.INVALID_PARAMETER;
import static com.sensordata.demo.exceptions.ErrorCode.INVALID_UUID;

@RestController
public class SensorController {

    @Autowired
    SensorService sensorService;

    @GetMapping("index")
    public String test() {
        return "Hello";
    }

    /**
     * @param request
     * @param response
     * @param uuid
     * @param sensorInput
     * @return
     * @throws Exception
     */
    @PostMapping("/api/v1/sensors/{uuid}/measurements")
    public ResponseEntity<Object> captureMeasurement(HttpServletRequest request, HttpServletResponse response,
                                                     @PathVariable(value = "uuid") String uuid, @RequestBody SensorInput sensorInput) {

        HashMap<String, String> resultMap = new HashMap<String, String>();

        // validate input
        try {
            // Please feel free to relax the validation on UUID "if required" by commenting
            // it.
            validateUUID(uuid);
            validateSensorInput(sensorInput);
            sensorService.sendMeasurement(uuid, sensorInput.getCo2(), sensorInput.getTime());
        } catch (Exception e) {
            resultMap.put("errorMessage", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private void validateUUID(String uuid) throws SystemException {
        try {
            UUID validateUUID = UUID.fromString(uuid);
        } catch (Exception e) {
            throw new SystemException(INVALID_UUID, "Invalid UUID : " + uuid);
        }

        if (uuid.length() != 36) {
            throw new SystemException(ErrorCode.INVALID_UUID);
        }

    }

    /**
     * @param request
     * @param response
     * @param uuid
     * @return
     */

    @GetMapping("/api/v1/sensors/{uuid}")
    public ResponseEntity<Object> getSensorCurrentStatus(HttpServletRequest request, HttpServletResponse response,
                                                         @PathVariable(value = "uuid") String uuid) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        Sensor sensor = sensorService.getSensor(uuid);
        if (sensor == null) {
            String errorMessage = "Sensor uuid: " + uuid + " not found";
            resultMap.put("errorMessage", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }
        resultMap.put("status", sensor.getCurrentStatus().toString());
        return ResponseEntity.status(HttpStatus.OK).body(resultMap);
    }

    /**
     * @param request
     * @param response
     * @param uuid
     * @return list of metrics for given sensor
     */
    @GetMapping("/api/v1/sensors/{uuid}/metrics")
    public ResponseEntity<Object> getSensorMetrics(HttpServletRequest request, HttpServletResponse response,
                                                   @PathVariable(value = "uuid") String uuid) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        Sensor sensor = sensorService.getSensor(uuid);
        if (sensor == null) {
            String errorMessage = "Sensor uuid: " + uuid + " not found";
            resultMap.put("errorMessage", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        Map<String, String> sensorMetrics = sensorService.getMetrics(sensor);
        return ResponseEntity.status(HttpStatus.OK).body(sensorMetrics);
    }

    /**
     * @param request
     * @param response
     * @param uuid
     * @return list of alerts for given sensor
     */
    @GetMapping("/api/v1/sensors/{uuid}/alerts")
    public ResponseEntity<Object> getSensorAlerts(HttpServletRequest request, HttpServletResponse response,
                                                  @PathVariable(value = "uuid") String uuid) throws Exception {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        Sensor sensor = sensorService.getSensor(uuid);
        if (sensor == null) {
            String errorMessage = "Sensor uuid: " + uuid + " not found";
            resultMap.put("errorMessage", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        List<String> alerts = sensorService.getAlerts(sensor);

        return ResponseEntity.status(HttpStatus.OK).body(JsonUtil.removeTrailingSlash(alerts));
    }

    @GetMapping("/api/v1/sensors/{uuid}/delete-sensor-data")
    public String deleteSensorData(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable(value = "uuid") String uuid) throws Exception {

        Sensor sensor = sensorService.getSensor(uuid);
        sensorService.deleteSensorData(sensor);
        return "success";
    }

    /**
     * @param sensorInput
     * @throws EmptyOrInvalidTimeFormatException
     */
    private void validateSensorInput(SensorInput sensorInput)
            throws SystemException {
        if (sensorInput.getCo2() == null) {
            throw new SystemException(INVALID_PARAMETER, "cO2 value must be provided");
        }
        if (sensorInput.getTime() == null) {
            throw new SystemException(INVALID_PARAMETER, "Time value must be provided");
        }

        if (sensorInput.getTime().isEmpty() || DateUtil.convertToDate(sensorInput.getTime()) == null) {
            throw new SystemException(INVALID_PARAMETER,"Time cannot be empty or in wrong format");
        }

    }
}
