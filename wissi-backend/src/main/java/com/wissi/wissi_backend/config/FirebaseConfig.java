package com.wissi.wissi_backend.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setDatabaseUrl("https://wissi-9a1ae-default-rtdb.firebaseio.com")
                        .build();
                
                FirebaseApp app = FirebaseApp.initializeApp(options);
                System.out.println("Firebase application initialized successfully");
                return app;
            }
            return FirebaseApp.getInstance();
        } catch (Exception e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    @Bean
    public DatabaseReference databaseReference() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            System.out.println("DatabaseReference created successfully");
            return ref;
        } catch (Exception e) {
            System.err.println("Error creating DatabaseReference: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create DatabaseReference", e);
        }
    }
}
