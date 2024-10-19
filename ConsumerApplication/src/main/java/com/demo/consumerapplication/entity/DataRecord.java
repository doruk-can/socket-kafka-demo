package com.demo.consumerapplication.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "data_records")
public class DataRecord {

    @Id
    private String id;
    private Long timestamp;
    private Integer randomInt;
    private String hash;
    private List<DataRecord> nestedRecords;

    public DataRecord(Long timestamp, Integer randomInt, String hash) {
        this.timestamp = timestamp;
        this.randomInt = randomInt;
        this.hash = hash;
        this.nestedRecords = new ArrayList<>();
    }

}