package com.eastwoo.socketkeyapi.websocket;

import com.eastwoo.socketkeyapi.websocket.handeler.WebSocketHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

/**
 * Please explain the class!!
 *
 * @author : dongwoo
 * @fileName : HistoricalDataProcessor
 * @since : 2024-09-10
 */
@Configuration
public class HistoricalDataSimulator {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSS");
    private final WebSocketHandler webSocketHandler;

    @Value("${fileReader.jsonDirectory}")
    private String DIRECTORY_PATH;

    public HistoricalDataSimulator(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Bean
    public CommandLineRunner simulateDataBroadcastRunner() {
        return args -> simulateDataBroadcast();
    }

    private void simulateDataBroadcast() {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.isDirectory()) {
            System.err.println("지정된 경로는 디렉토리가 아닙니다.");
            return;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.err.println("디렉토리 내에 JSON 파일이 없습니다.");
            return;
        }

        Arrays.sort(files, Comparator.comparingLong(File::lastModified));

        ObjectMapper mapper = new ObjectMapper();
        while (true) {
            for (File file : files) {
                LocalDateTime lastTimestamp = null;
                System.out.println("Processing file: " + file.getName());
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty()) {
                            continue;
                        }

                        JsonNode record = mapper.readTree(line);
                        String tm = record.path("frm").path("tm").asText();

                        if (tm.isEmpty()) {
                            continue;
                        }

                        LocalDateTime currentTimestamp = LocalDateTime.parse(tm, DATE_TIME_FORMATTER);
                        if (lastTimestamp != null) {
                            long delay = ChronoUnit.MILLIS.between(lastTimestamp, currentTimestamp);
                            if (delay > 0) {
                                TimeUnit.MILLISECONDS.sleep(delay);
                            }
                        }

                        // Send the data to all WebSocket clients
                        webSocketHandler.broadcastMessage(line);

                        lastTimestamp = currentTimestamp;
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Optional: Add a delay before starting over
            try {
                TimeUnit.SECONDS.sleep(5); // Adjust as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}