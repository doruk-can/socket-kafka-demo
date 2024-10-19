package com.demo.consumerapplication.model.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataEvent {

    private long timestamp;
    private int randomInt;
    private String hash;

}
