package com.wissi.wissi_backend.config;

<<<<<<< HEAD
=======
import com.google.auth.oauth2.GoogleCredentials;
>>>>>>> 6ba9c49 (deploy: backend with mock service for production - no secrets)
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
<<<<<<< HEAD
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
=======
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
>>>>>>> 6ba9c49 (deploy: backend with mock service for production - no secrets)
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
<<<<<<< HEAD
                FirebaseOptions options = FirebaseOptions.builder()
                        .setDatabaseUrl("https://wissi-9a1ae-default-rtdb.firebaseio.com")
                        .build();
                
                FirebaseApp app = FirebaseApp.initializeApp(options);
                System.out.println("Firebase application initialized successfully");
=======
                System.out.println("Initializing Firebase...");
                
                FirebaseOptions.Builder optionsBuilder = FirebaseOptions.builder()
                        .setDatabaseUrl("https://wissi-9a1ae-default-rtdb.firebaseio.com");
                
                // Cargar credenciales desde archivo
                try {
                    ClassPathResource resource = new ClassPathResource("firebase-key-temp.json");
                    if (resource.exists()) {
                        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
                        optionsBuilder.setCredentials(credentials);
                        System.out.println("✅ Firebase credentials loaded successfully");
                    } else {
                        throw new RuntimeException("firebase-key-temp.json not found in resources");
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load Firebase credentials: " + e.getMessage());
                }

                FirebaseOptions options = optionsBuilder.build();
                FirebaseApp app = FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase application initialized successfully");
                System.out.println("📡 Database URL: " + options.getDatabaseUrl());
>>>>>>> 6ba9c49 (deploy: backend with mock service for production - no secrets)
                return app;
            }
            return FirebaseApp.getInstance();
        } catch (Exception e) {
<<<<<<< HEAD
            System.err.println("Error initializing Firebase: " + e.getMessage());
=======
            System.err.println("❌ Error initializing Firebase: " + e.getMessage());
>>>>>>> 6ba9c49 (deploy: backend with mock service for production - no secrets)
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    @Bean
    public DatabaseReference databaseReference() {
        try {
<<<<<<< HEAD
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            System.out.println("DatabaseReference created successfully");
            return ref;
        } catch (Exception e) {
            System.err.println("Error creating DatabaseReference: " + e.getMessage());
=======
            FirebaseApp app = firebaseApp();
            FirebaseDatabase database = FirebaseDatabase.getInstance(app);
            DatabaseReference ref = database.getReference();
            System.out.println("✅ DatabaseReference created successfully");
            return ref;
        } catch (Exception e) {
            System.err.println("❌ Error creating DatabaseReference: " + e.getMessage());
>>>>>>> 6ba9c49 (deploy: backend with mock service for production - no secrets)
            e.printStackTrace();
            throw new RuntimeException("Failed to create DatabaseReference", e);
        }
    }
}
