package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/message", produces = "application/json")
public class MessageRestController {

    @Autowired
    private MessageRepo messageRepo;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/all")
    public ResponseEntity<Iterable<Message>> getListMessages() {
        Iterable<Message> messages = messageRepo.findAll();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Message> addMessage(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Message message = new Message(title, content);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            // загружаем файл
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadPath + "/" + resultFileName);
            Files.write(path, bytes);
            message.setImg(resultFileName);
        }

        messageRepo.save(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
