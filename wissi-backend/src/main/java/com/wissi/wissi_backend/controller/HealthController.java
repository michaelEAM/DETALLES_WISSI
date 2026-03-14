package com.wissi.wissi_backend.controller;

import org.springframework.web.bind.annotation.*;
import com.wissi.wissi_backend.service.FirebaseServiceInterface;

import java.util.concurrent.CompletableFuture;

@RestController
public class HealthController {

    private final FirebaseServiceInterface firebaseService;

    // Inyección por constructor (mejor práctica)
    public HealthController(FirebaseServiceInterface firebaseService) {
        this.firebaseService = firebaseService;
    }

    // Verificar si el backend está vivo
    @GetMapping("/health")
    public String health() {
        return "✅ Backend is running!";
    }

    // Verificar si Firebase se cargó correctamente
    @GetMapping("/debug")
    public String debug() {

        StringBuilder sb = new StringBuilder();

        sb.append("🔥 Backend Debug Info:\n");

        if (firebaseService != null) {
            sb.append("Firebase Service: ✅ Loaded\n");
            sb.append("Service Type: ")
                    .append(firebaseService.getClass().getSimpleName())
                    .append("\n");
        } else {
            sb.append("Firebase Service: ❌ Not loaded\n");
        }

        return sb.toString();
    }

    // Probar acceso a datos
    @GetMapping("/test-data")
    public CompletableFuture<String> testData() {

        return firebaseService.getCategories()
                .thenApply(categories ->
                        "📊 Categories loaded: " + categories.size()
                )
                .exceptionally(error ->
                        "❌ Error loading data: " + error.getMessage()
                );
    }
}
