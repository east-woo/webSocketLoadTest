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
 * 이 클래스는 히스토리컬 데이터를 WebSocket을 통해 클라이언트에 시뮬레이션하여 전송하는 기능을 담당한다.
 *
 * @author : dongwoo
 * @fileName : HistoricalDataProcessor
 * @since : 2024-09-10
 */
@Configuration
public class HistoricalDataSimulator {

    // 타임스탬프 형식을 지정하기 위한 DateTimeFormatter (yyyyMMdd'T'HHmmss.SSS 형식)
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSS");

    // WebSocket을 통해 데이터를 브로드캐스트하기 위한 핸들러
    private final WebSocketHandler webSocketHandler;

    // JSON 파일이 위치한 디렉토리 경로를 지정하는 Spring Value 어노테이션
    @Value("${fileReader.jsonDirectory}")
    private String DIRECTORY_PATH;

    // 생성자를 통해 WebSocketHandler 주입
    public HistoricalDataSimulator(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    // Spring Boot 애플리케이션 실행 시 시뮬레이션 데이터를 전송하는 CommandLineRunner 빈을 생성
    @Bean
    public CommandLineRunner simulateDataBroadcastRunner() {
        return args -> simulateDataBroadcast();
    }

    // 시뮬레이션 데이터를 브로드캐스트하는 메소드
    private void simulateDataBroadcast() {
        // JSON 파일들이 저장된 디렉토리를 가져옴
        File directory = new File(DIRECTORY_PATH);

        // 디렉토리가 맞는지 확인
        if (!directory.isDirectory()) {
            System.err.println("지정된 경로는 디렉토리가 아닙니다.");
            return;
        }

        // 디렉토리 내의 모든 JSON 파일을 가져옴
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));

        // JSON 파일이 없으면 에러 메시지 출력
        if (files == null || files.length == 0) {
            System.err.println("디렉토리 내에 JSON 파일이 없습니다.");
            return;
        }

        // 파일들을 수정된 시간순으로 정렬
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));

        // Jackson ObjectMapper를 사용하여 JSON 데이터를 파싱
        ObjectMapper mapper = new ObjectMapper();

        // 무한 루프로 시뮬레이션 반복 실행
        while (true) {
            // 각 JSON 파일에 대해 반복 처리
            for (File file : files) {
                LocalDateTime lastTimestamp = null; // 이전 타임스탬프를 저장하기 위한 변수
                System.out.println("Processing file: " + file.getName()); // 현재 처리 중인 파일 출력

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;

                    // 파일 내의 각 라인을 읽어 처리
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty()) { // 빈 줄은 건너뜀
                            continue;
                        }

                        // JSON 문자열을 JsonNode 객체로 변환
                        JsonNode record = mapper.readTree(line);
                        String tm = record.path("frm").path("tm").asText(); // 타임스탬프 필드 추출

                        // 타임스탬프가 없는 레코드는 건너뜀
                        if (tm.isEmpty()) {
                            continue;
                        }

                        // 현재 타임스탬프를 LocalDateTime으로 파싱
                        LocalDateTime currentTimestamp = LocalDateTime.parse(tm, DATE_TIME_FORMATTER);

                        // 이전 타임스탬프가 존재하면 두 타임스탬프 사이의 지연 시간을 계산
                        if (lastTimestamp != null) {
                            long delay = ChronoUnit.MILLIS.between(lastTimestamp, currentTimestamp);
                            if (delay > 0) {
                                // 지연 시간을 반영하여 현재 스레드를 멈춤
                                TimeUnit.MILLISECONDS.sleep(delay);
                            }
                        }

                        // WebSocket을 통해 모든 클라이언트에게 데이터 브로드캐스트
                        webSocketHandler.broadcastMessage(line);

                        // 현재 타임스탬프를 이전 타임스탬프 변수에 저장
                        lastTimestamp = currentTimestamp;
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace(); // 예외 발생 시 스택 트레이스 출력
                }
            }

            // 모든 파일이 처리된 후 5초 지연 (필요 시 조정 가능)
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
