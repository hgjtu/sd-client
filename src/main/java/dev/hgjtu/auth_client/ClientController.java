package dev.hgjtu.auth_client;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class ClientController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User != null) {
            model.addAttribute("name", oauth2User.getAttribute("name"));
            model.addAttribute("email", oauth2User.getAttribute("email"));
            model.addAttribute("sub", oauth2User.getAttribute("sub"));
            model.addAttribute("attributes", oauth2User.getAttributes());
        }
        return "dashboard";
    }

    // Обработчик для страницы ошибок
    @GetMapping("/error")
    public String error(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            model.addAttribute("status", status.toString());
        }
        return "error";
    }

    @GetMapping("/api-data")
    public String getApiData(Model model,
                             @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {

        String resourceUrl = "http://localhost:7070/api/protected";

        String response = restTemplate.getForObject(resourceUrl, String.class);
        model.addAttribute("apiData", response);

        return "api-data";
    }
}
