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
                System.out.println(" Inicializando Firebase...");

                String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS");
                
                // Si no hay credenciales, inicializar sin ellas (para desarrollo local)
                if (firebaseCredentials == null || firebaseCredentials.trim().isEmpty()) {
                    System.out.println(" FIREBASE_CREDENTIALS no definido, usando modo sin autenticación");
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setDatabaseUrl("https://wissi-9a1ae-default-rtdb.firebaseio.com")
                            .build();
                    return FirebaseApp.initializeApp(options);
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
            System.err.println(" Error inicializando Firebase: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Bean
    public DatabaseReference databaseReference(FirebaseApp app) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance(app);
            DatabaseReference ref = database.getReference();
            System.out.println(" DatabaseReference bean creado exitosamente");
            return ref;
        } catch (Exception e) {
            System.err.println(" Error creando DatabaseReference: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}