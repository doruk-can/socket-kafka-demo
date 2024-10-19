package com.demo.datageneratorapplication.service;

import com.demo.datageneratorapplication.socket.SocketClient;
import com.demo.datageneratorapplication.model.event.DataEvent;
import com.demo.datageneratorapplication.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataGeneratorService {

    private final JsonUtil jsonUtil;
    private final SocketClient socketClient;
    private final Random random = new Random();


    @Scheduled(fixedRate = 200)
    public void generateData() {
        try {
            long timestamp = Instant.now().toEpochMilli();
            int randomInt = random.nextInt(101);
            String hash = generateHash(timestamp, randomInt);

            DataEvent dataEvent = DataEvent.builder()
                    .timestamp(timestamp)
                    .randomInt(randomInt)
                    .hash(hash)
                    .build();

            String jsonData = jsonUtil.toJson(dataEvent);

            socketClient.sendData(jsonData);

            log.info("Generated and sent data: {}", jsonData);
        } catch (Exception e) {
            log.error("Error generating data", e);
        }
    }

    private String generateHash(long timestamp, int randomInt) throws NoSuchAlgorithmException {
        String input = timestamp + String.valueOf(randomInt);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes());

        String hexString = HexFormat.of().formatHex(digest);

        return hexString.substring(hexString.length() - 2);
    }
}
