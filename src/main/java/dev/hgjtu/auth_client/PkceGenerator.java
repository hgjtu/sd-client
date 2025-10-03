package dev.hgjtu.auth_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class PkceGenerator implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PkceGenerator.class);

    @Override
    public void run(String... args) throws Exception {
        generatePkceCodes();
    }

    public Map<String, String> generatePkceCodes() throws NoSuchAlgorithmException {
        // Генерация code_verifier
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        String verifier = Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);

        // Генерация code_challenge
        byte[] bytes = verifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        String challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);

        Map<String, String> pkceData = new HashMap<>();
        pkceData.put("code_verifier", verifier);
        pkceData.put("code_challenge", challenge);
        pkceData.put("method", "S256");

        logger.info("PKCE Code Verifier: " + verifier);
        logger.info("PKCE Code Challenge: " + challenge);

        return pkceData;
    }
}