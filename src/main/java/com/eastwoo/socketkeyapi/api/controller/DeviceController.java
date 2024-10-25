package com.eastwoo.socketkeyapi.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Please explain the class!!
 *
 * @author : dongwoo
 * @fileName : DeviceController
 * @since : 2024-09-23
 */
@Slf4j
@RestController
public class DeviceController {

    @GetMapping("/devices/vaChannels")
    public String getVaChannels(@RequestHeader(value = "api-key") String apiKey) {
        log.info("Get device vaChannels: {}", apiKey);
        try {
            Resource resource = new ClassPathResource("metadata/video_mata_205.json");
            Path path = Paths.get(resource.getURI());
            log.info("path: {}", path);
            return new String(Files.readAllBytes(path));
        } catch (Exception e) {
            return "Error reading the file: " + e.getMessage();
        }
    }
}