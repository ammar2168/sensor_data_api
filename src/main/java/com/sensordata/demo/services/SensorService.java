package com.sensordata.demo.services;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sensordata.demo.entities.Sensor;
import com.sensordata.demo.exceptions.SystemException;


public interface SensorService {

    //for getting sensor for a given uuid
    public Sensor getSensor(String uuid);

    //for sending measurement to sensor processing service
    public void sendMeasurement(String uuid, int level, String dateStr) throws SystemException;

    //for getting metrics in last 30 days for given sensor
    public Map<String,String> getMetrics(Sensor sensor);

    //for getting alerts for a given sensor
    public List<String> getAlerts(Sensor sensor);

    public void deleteSensorData(Sensor sensor);

}
