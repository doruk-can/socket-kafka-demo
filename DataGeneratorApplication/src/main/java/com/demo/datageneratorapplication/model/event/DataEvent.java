package com.demo.datageneratorapplication.model.event;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataEvent {

    private long timestamp;
    private int randomInt;
    private String hash;

}
