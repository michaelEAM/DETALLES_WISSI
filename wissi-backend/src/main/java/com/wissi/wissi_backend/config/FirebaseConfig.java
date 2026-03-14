package com.wissi.wissi_backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {

            if (FirebaseApp.getApps().isEmpty()) {

                System.out.println("🔥 Initializing Firebase REAL Database...");

                String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS");

                // arreglar saltos de línea de la clave privada
                firebaseCredentials = firebaseCredentials.replace("\\n", "\n");



                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                                new ByteArrayInputStream(firebaseCredentials.getBytes())))
                        .setDatabaseUrl("https://wissi-9a1ae-default-rtdb.firebaseio.com")
                        .build();

                FirebaseApp app = FirebaseApp.initializeApp(options);

                System.out.println("✅ Firebase initialized successfully");

                return app;
            }

            return FirebaseApp.getInstance();

        } catch (Exception e) {

            System.err.println("❌ Error initializing Firebase: " + e.getMessage());

            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    @Bean
    public DatabaseReference databaseReference() {

        FirebaseApp app = firebaseApp();

        FirebaseDatabase database = FirebaseDatabase.getInstance(app);

        return database.getReference();
    }
}