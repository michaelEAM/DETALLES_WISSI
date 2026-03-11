package com.wissi.wissi_backend.config;

import com.wissi.wissi_backend.service.FirebaseService;
import com.wissi.wissi_backend.service.MockFirebaseService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockConfig {

    @Bean
    @Primary
    public FirebaseService firebaseService(MockFirebaseService mockFirebaseService) {
        System.out.println("🔥 Using Mock Firebase Service for development");
        return new FirebaseService() {
            @Override
            public java.util.concurrent.CompletableFuture<java.util.List<java.util.Map<String, Object>>> getCategories() {
                return mockFirebaseService.getCategories();
            }

            @Override
            public java.util.concurrent.CompletableFuture<String> addCategory(java.util.Map<String, Object> category) {
                return mockFirebaseService.addCategory(category);
            }

            @Override
            public java.util.concurrent.CompletableFuture<Void> updateCategory(String id, java.util.Map<String, Object> data) {
                return mockFirebaseService.updateCategory(id, data);
            }

            @Override
            public java.util.concurrent.CompletableFuture<Void> deleteCategory(String id) {
                return mockFirebaseService.deleteCategory(id);
            }

            @Override
            public java.util.concurrent.CompletableFuture<java.util.List<java.util.Map<String, Object>>> getProducts() {
                return mockFirebaseService.getProducts();
            }

            @Override
            public java.util.concurrent.CompletableFuture<String> addProduct(java.util.Map<String, Object> product) {
                return mockFirebaseService.addProduct(product);
            }

            @Override
            public java.util.concurrent.CompletableFuture<Void> updateProduct(String id, java.util.Map<String, Object> data) {
                return mockFirebaseService.updateProduct(id, data);
            }

            @Override
            public java.util.concurrent.CompletableFuture<Void> deleteProduct(String id) {
                return mockFirebaseService.deleteProduct(id);
            }
        };
    }
}
