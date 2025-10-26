package dev.hgjtu.auth_client.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/communication")
public class CommunicationController {

    @GetMapping("/")
    public String home() {
        return "communication/main-page";
    }

}
