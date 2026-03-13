package com.wissi.wissi_backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {

                System.out.println(" Initializing Firebase REAL Database...");

                FirebaseOptions.Builder optionsBuilder = FirebaseOptions.builder()
                        .setDatabaseUrl("https://wissi-9a1ae-default-rtdb.firebaseio.com");

                // Para producción, usaremos database sin autenticación temporalmente
                // En el futuro, puedes agregar credenciales aquí
                
                FirebaseOptions options = optionsBuilder.build();

                FirebaseApp app = FirebaseApp.initializeApp(options);

                System.out.println(" Firebase REAL application initialized successfully");
                System.out.println(" Database URL: " + options.getDatabaseUrl());

                return app;
            }

            return FirebaseApp.getInstance();

        } catch (Exception e) {

            System.err.println(" Error initializing Firebase: " + e.getMessage());
            e.printStackTrace();

            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    @Bean
    public DatabaseReference databaseReference() {

        try {

            FirebaseApp app = firebaseApp();

            FirebaseDatabase database = FirebaseDatabase.getInstance(app);

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