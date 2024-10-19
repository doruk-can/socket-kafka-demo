package com.demo.consumerapplication.service;

import com.demo.consumerapplication.entity.DataRecord;
import com.demo.consumerapplication.model.event.DataEvent;
import com.demo.consumerapplication.mapper.DataMapper;
import com.demo.consumerapplication.repository.DataRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {


    private final DataRecordRepository mongoDataRecordRepository;
    private DataRecord currentRecord; // needs modification for multiple instances or to ensure durability across application restarts
    private final DataMapper dataMapper;
    private static final int COMPARING_VALUE = 0x99;


    @KafkaListener(topics = "${topic.data-topic}", groupId = "data-consumer-group")
    public void dataListener(DataEvent dataEvent) {
        log.info("Received event: {}", dataEvent);

        DataRecord dataRecord = dataMapper.eventToEntity(dataEvent);

        if (isHashGreaterThan(dataEvent.getHash(), COMPARING_VALUE)) {
            if (currentRecord != null) {
                currentRecord.getNestedRecords().add(dataRecord);
                mongoDataRecordRepository.save(currentRecord);
                log.info("Nested data added to current record: {}", currentRecord);
            } else {
                currentRecord = dataRecord;
                mongoDataRecordRepository.save(currentRecord);
                log.info("New record created: {}", currentRecord);
            }
        } else {
            currentRecord = dataRecord;
            mongoDataRecordRepository.save(currentRecord);
            log.info("New record created: {}", currentRecord);
        }
    }

    private boolean isHashGreaterThan(String hash, int value) {
        try {
            int hashValue = Integer.parseInt(hash, 16);
            return hashValue > value;
        } catch (NumberFormatException e) {
            log.warn("Invalid hash format: {}", hash, e);
            return false;
        }
    }
}