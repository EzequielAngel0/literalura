package com.alura.literalura.service;

import com.alura.literalura.dto.GutendexResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GutendexService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BASE_URL = "https://gutendex.com/books/";

    public GutendexService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper(); // Manual instantiation
    }

    public GutendexResponseDTO fetchBooks(String title) {
        String encodedTitle = title.replace(" ", "%20");
        String url = BASE_URL + "?search=" + encodedTitle;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), GutendexResponseDTO.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error fetching books from Gutendex API", e);
        }
    }
}
