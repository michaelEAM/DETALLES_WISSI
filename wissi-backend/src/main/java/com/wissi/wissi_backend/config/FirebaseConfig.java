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
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                System.out.println("🔥 Inicializando Firebase...");

                String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS");
                if (firebaseCredentials == null) {
                    throw new RuntimeException("FIREBASE_CREDENTIALS no está definido en Render");
                }
                firebaseCredentials = firebaseCredentials.replace("\\n", "\n");

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                                new ByteArrayInputStream(firebaseCredentials.getBytes())))
                        .setDatabaseUrl("https://wissi-9a1ae-default-rtdb.firebaseio.com")
                        .build();

                return FirebaseApp.initializeApp(options);
            }
            return FirebaseApp.getInstance();
        } catch (Exception e) {
            System.err.println("❌ Error inicializando Firebase: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Bean
    public DatabaseReference databaseReference(FirebaseApp app) {
        return FirebaseDatabase.getInstance(app).getReference();
    }
}