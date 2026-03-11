package com.wissi.wissi_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.wissi.wissi_backend.service.FirebaseService;
import java.util.concurrent.CompletableFuture;

@RestController
public class HealthController {

    @Autowired
    private FirebaseService firebaseService;

    @GetMapping("/health")
    public String health() {
        return "✅ Backend is running!";
    }

    @GetMapping("/debug")
    public String debug() {
        StringBuilder sb = new StringBuilder();
        sb.append("🔥 Backend Debug Info:\n");
        sb.append("Firebase Service: ").append(firebaseService != null ? "✅ Loaded" : "❌ Not loaded").append("\n");
        sb.append("Service Type: ").append(firebaseService.getClass().getSimpleName()).append("\n");
        return sb.toString();
    }

    @GetMapping("/test-data")
    public CompletableFuture<String> testData() {
        return firebaseService.getCategories()
            .thenApply(categories -> "📊 Categories loaded: " + categories.size())
            .exceptionally(throwable -> "❌ Error: " + throwable.getMessage());
    }
}
