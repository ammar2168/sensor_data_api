package com.sensordata.demo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensordata.demo.entities.SensorMeasurementEvent;
import com.sensordata.demo.exceptions.ErrorCode;
import com.sensordata.demo.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String topic = "sensorEvents";

    private ObjectMapper objectMapper = new ObjectMapper();

    public void produce(SensorMeasurementEvent event) throws SystemException {

        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new SystemException(ErrorCode.PARSE_ERROR, "Error while converting event to json.");
        }

    }

}
