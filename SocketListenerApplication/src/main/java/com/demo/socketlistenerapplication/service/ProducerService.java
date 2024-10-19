package com.demo.socketlistenerapplication.service;

import com.demo.socketlistenerapplication.model.event.DataEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final FileWriterService fileWriterService;

    @Value("${topic.data-topic}")
    private String dataTopic;

    public void processData(String jsonData) {
        log.debug("Received data: {}", jsonData);

        try {
            DataEvent dataEvent = objectMapper.readValue(jsonData, DataEvent.class);

            if (dataEvent.getRandomInt() > 90) {
                kafkaTemplate.send(dataTopic, dataEvent);
                log.info("Data sent to Kafka: {}", dataEvent);
            } else {
                fileWriterService.appendToFile(jsonData);
                log.info("Data appended to file: {}", jsonData);
            }
        } catch (JsonProcessingException e) {
            log.error("Error deserializing JSON data: {}", jsonData, e);
        } catch (IOException e) {
            log.error("Error writing data to file", e);
        }
    }
}
