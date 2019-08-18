package com.sensordata.demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sensordata.demo.entities.EventDto;
import com.sensordata.demo.entities.Sensor;
import com.sensordata.demo.producer.EventProducer;
import com.sensordata.demo.repository.AlertRepository;
import com.sensordata.demo.repository.MeasurementRepository;
import com.sensordata.demo.repository.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SensorServiceImpl implements SensorService {

	@Autowired
	SensorRepository sensorRepository;

	@Autowired
	MeasurementRepository measurementRepository;

	@Autowired
	AlertRepository alertRepository;

	@Autowired
	private EventProducer producer;

	@Override
	public Sensor getSensor(String uuid) {
		return sensorRepository.getSensorByUUID(uuid);
	}

	@Override
	public void sendMeasurement(String uuid, int level, String dateStr) throws JsonProcessingException {

		EventDto eventDto = new EventDto(uuid, level, dateStr);
		producer.produce(eventDto);
	}

	@Override
	public Map<String, String> getMetrics(Sensor sensor) {
		List<Object[]> results = measurementRepository.getAvgAndMax(sensor.getId());

		HashMap<String, String> map = new HashMap<>();
		map.put("maxLast30Days", results.get(0)[1].toString());
		map.put("avgLast30Days", results.get(0)[0].toString());
		return map;
	}

	@Override
	public List<String> getAlerts(Sensor sensor) {
		List<String> alerts = alertRepository.getAlerts(sensor.getId());
		return alerts;
	}

	@Override
	public void deleteSensorData(Sensor sensor) {

		alertRepository.deleteMeasurements(sensor.getId());
		measurementRepository.deleteMeasurements(sensor.getId());
		sensorRepository.deleteSensor(sensor.getUuid());

	}

}
