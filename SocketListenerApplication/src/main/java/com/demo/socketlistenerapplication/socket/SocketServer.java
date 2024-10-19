package com.demo.socketlistenerapplication.socket;

import com.demo.socketlistenerapplication.service.ProducerService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketServer {


    @Value("${socket.port}")
    private int port;

    private final ProducerService producerService;

    private ServerSocket serverSocket;
    private volatile boolean running = true;


    @PostConstruct
    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            log.info("Socket server started on port {}", port);

            Thread listenerThread = new Thread(this::listen);
            listenerThread.start();
        } catch (IOException e) {
            log.error("Failed to start socket server", e);
        }
    }

    @PreDestroy
    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
            log.info("Socket server stopped");
        } catch (IOException e) {
            log.error("Failed to stop socket server", e);
        }
    }

    private void listen() {
        while (running) {
            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream()))) {

                String jsonData;
                while ((jsonData = reader.readLine()) != null) {
                    producerService.processData(jsonData);
                }
            } catch (IOException e) {
                if (running) {
                    log.error("Error in socket server", e);
                }
            }
        }
    }

}