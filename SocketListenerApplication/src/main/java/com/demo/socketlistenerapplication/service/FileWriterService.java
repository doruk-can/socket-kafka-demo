package com.demo.socketlistenerapplication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileWriterService {

    @Value("${file.output}")
    private String outputFilePath;

    public void appendToFile(String data) throws IOException {
        File file = new File(outputFilePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(data);
            bw.newLine();
        }
    }

}
