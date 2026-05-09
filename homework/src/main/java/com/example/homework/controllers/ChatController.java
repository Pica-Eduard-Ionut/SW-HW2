package com.example.homework.controllers;

import com.example.homework.models.ChatRequest;
import com.example.homework.models.ChatResponse;
import com.example.homework.services.GoogleAiClient;
import com.example.homework.services.RagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatController {

    private final RagService ragService;
    private final GoogleAiClient aiClient;

    public ChatController(RagService ragService, GoogleAiClient aiClient) {
        this.ragService  = ragService;
        this.aiClient    = aiClient;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        List<String> context = ragService.search(request.getMessage(), 3);

        String systemPrompt =
            "You are a helpful book recommendation assistant for a small library. " +
            "Answer questions ONLY based on the book data provided below. " +
            "Do not use your own knowledge about books — if a book is not in the list, say it is not in the database. " +
            "Keep answers concise.\n\n" +
            "Books in the database:\n" + String.join("\n", context);

        String reply = aiClient.chat(systemPrompt, request.getMessage());
        return new ChatResponse(reply);
    }

    @GetMapping("/starters")
    public Map<String, List<String>> starters(
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String bookId) {

        List<String> starters;

        if ("bookDetail".equals(page) && bookId != null && !bookId.isBlank()) {
            String name = bookId.contains("/") ? bookId.substring(bookId.lastIndexOf('/') + 1) : bookId;
            starters = List.of(
                "Who wrote " + name + "?",
                "What themes does " + name + " cover?",
                "Who would enjoy reading " + name + "?"
            );
        } else if ("books".equals(page)) {
            starters = List.of(
                "What book am I most likely to enjoy from this list?",
                "Recommend a Science Fiction book",
                "Which books are suitable for beginners?"
            );
        } else {
            starters = List.of(
                "What books do you have?",
                "Help me find a book",
                "Recommend something for a beginner"
            );
        }

        return Map.of("starters", starters);
    }
}
