package com.demo.datageneratorapplication.socket;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class SocketClient {

    @Value("${socket.host}")
    private String host;

    @Value("${socket.port}")
    private int port;

    private volatile Socket socket;
    private volatile BufferedWriter writer;

    private final ReentrantLock lock = new ReentrantLock();

    public SocketClient() {
    }

    public void connect() {
        lock.lock();
        try {
            if (socket == null || socket.isClosed()) {
                try {
                    socket = new Socket(host, port);
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    log.info("Socket connection established with {}:{}", host, port);
                } catch (IOException e) {
                    log.error("Unable to establish socket connection", e);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @PreDestroy
    public void cleanup() {
        lock.lock();
        try {
            if (writer != null) writer.close();
            if (socket != null) socket.close();
            log.info("Socket connection closed");
        } catch (IOException e) {
            log.error("Failed to close resources", e);
        } finally {
            lock.unlock();
        }
    }

    public void sendData(String data) {
        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            if (socket == null || socket.isClosed() || writer == null) {
                connect();
            }

            lock.lock();
            try {
                if (writer != null) {
                    writer.write(data);
                    writer.newLine();
                    writer.flush();
                    log.debug("Data sent: {}", data);
                    return;
                } else {
                    log.warn("Data not sent, socket connection is not established.");
                }
            } catch (IOException e) {
                log.error("Failed to send data, attempt {}", attempt + 1, e);
                try {
                    if (socket != null) socket.close();
                } catch (IOException ex) {
                    log.error("Failed to close socket during reconnection attempt", ex);
                }
                socket = null;
                writer = null;
            } finally {
                lock.unlock();
            }

            attempt++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.error("Thread interrupted during retry sleep", ie);
                break;
            }
        }

        log.error("Failed to send data after {} attempts", maxRetries);
    }


}
