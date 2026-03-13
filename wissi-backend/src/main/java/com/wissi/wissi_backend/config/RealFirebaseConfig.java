package com.wissi.wissi_backend.config;

import com.wissi.wissi_backend.service.FirebaseService;
import com.wissi.wissi_backend.service.FirebaseServiceImpl;
import com.google.firebase.database.DatabaseReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = false)
public class RealFirebaseConfig {

    @Bean
    @Primary
    public FirebaseService firebaseServiceReal(DatabaseReference databaseReference) {
        System.out.println("🔥 Using REAL Firebase Service");
        return new FirebaseServiceImpl(databaseReference);
    }
}
