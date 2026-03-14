package com.wissi.wissi_backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;

@Configuration
public class FirebaseFallbackConfig {

    @Bean
    @ConditionalOnMissingBean(FirebaseApp.class)
    public FirebaseApp fallbackFirebaseApp() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                System.out.println("🔥 Creando Firebase App de respaldo...");

                String firebaseCredentials = System.getenv("FIREBASE_CREDENTIALS");
                
                if (firebaseCredentials != null && !firebaseCredentials.trim().isEmpty()) {
                    firebaseCredentials = firebaseCredentials.replace("\\n", "\n");
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(
                                    new ByteArrayInputStream(firebaseCredentials.getBytes())))
                            .setDatabaseUrl("https://wissi-9a1ae-default-rtdb.firebaseio.com")
                            .build();
                    return FirebaseApp.initializeApp(options);
                } else {
                    System.out.println("⚠️ Sin credenciales, usando modo público");
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setDatabaseUrl("https://wissi-9a1ae-default-rtdb.firebaseio.com")
                            .build();
                    return FirebaseApp.initializeApp(options);
                }
            }
            return FirebaseApp.getInstance();
        } catch (Exception e) {
            System.err.println("❌ Error en Firebase fallback: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Bean
    @ConditionalOnMissingBean(DatabaseReference.class)
    public DatabaseReference fallbackDatabaseReference(FirebaseApp app) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance(app).getReference();
            System.out.println("✅ DatabaseReference de respaldo creado");
            return ref;
        } catch (Exception e) {
            System.err.println("❌ Error creando DatabaseReference de respaldo: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
