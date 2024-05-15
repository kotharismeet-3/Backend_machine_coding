package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

@Service
public class FileWatchService {

    private final RandomAccessFile randomAccessFile;
    private final String FILE_NAME = "log.txt";
    private final String READ_MODE = "r";
    private final String DESTINATION = "topic/log";
    private long offset;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public FileWatchService() throws FileNotFoundException, IOException {
        randomAccessFile = new RandomAccessFile(FILE_NAME, READ_MODE);
        offset = intialOffset();
    }

    @Scheduled(fixedDelay = 100, initialDelay = 5000)
    private void updateLogs() throws IOException {
        long fileLength = randomAccessFile.length(), lineCount = 0;
        randomAccessFile.seek(offset);

        while(randomAccessFile.getFilePointer() < fileLength && lineCount < 10) {
            String latestFileData = randomAccessFile.readLine();
            String payload = "{\"content\":\"" + latestFileData + "\"}";

            messagingTemplate.convertAndSend(DESTINATION,payload);
            lineCount++;
        }
    }

    private long intialOffset() throws IOException {
        int lineCount = 0;
        while(randomAccessFile.readLine() != null) {
            lineCount++;
        }

        if(lineCount > 10) {
            offset = lineCount - 10;
        }
        return offset;
    }

}