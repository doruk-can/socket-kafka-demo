# **Applications Overview**

## 1. Data Generator Application

This is the `DataGeneratorApplication` that generates random data and sends it to a socket. This application creates random data containing a timestamp, a random integer (0-100), and the hash of these values, which is then sent over the socket.

## 2. Socket Listener Application

This is the `SocketListenerApplication` that listens to data from a socket, filters the data, and either sends it to Kafka or appends it to a file. The application checks if the random integer value is above 90—if it is, the data is sent to Kafka, otherwise, it is written to a file.

## 3. Consumer Application

This is the `ConsumerApplication` that listens to Kafka events and processes data by storing it in MongoDB. 

---

## **Requirements**

- **JDK 21**
- **Docker**

---

## **Instructions**

After running `docker-compose.yaml` (which can be found in the `consumerApplication` project directory), the necessary services will be up and running.

### MongoDB Information

- You can connect to MongoDB at: localhost:27017

### Kafka UI Information

- You can check Kafka topics at: localhost:7070
