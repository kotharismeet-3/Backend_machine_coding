package com.example.demo.controller;

import com.example.demo.config.WebSocketConfig;
import com.example.demo.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class FileWatchController {

    @MessageMapping("/logs")
    @SendTo("/topic/log")
    public Message getFileUpdates(Message message) {
        return message;
    }
    // This can be also re-written as
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//    @MessageMapping("/logs")
//    public Message getFileUpdatesV2(Message message) {
//        return this.webSocketConfig.convertAndSent("/topic/logs", message);
//    }
}
