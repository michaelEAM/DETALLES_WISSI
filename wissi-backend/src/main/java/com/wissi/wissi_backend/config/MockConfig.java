package com.wissi.wissi_backend.config;

import com.wissi.wissi_backend.service.FirebaseService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "false")
public class MockConfig {

    @Bean
    @Primary
    public FirebaseService firebaseServiceMock() {

        return new FirebaseService() {

            @Override
            public CompletableFuture<List<Map<String, Object>>> getCategories() {
                return CompletableFuture.completedFuture(new ArrayList<>());
            }

            @Override
            public CompletableFuture<String> addCategory(Map<String, Object> category) {
                return CompletableFuture.completedFuture("mock-category-id");
            }

            @Override
            public CompletableFuture<Void> updateCategory(String id, Map<String, Object> data) {
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Void> deleteCategory(String id) {
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<List<Map<String, Object>>> getProducts() {
                return CompletableFuture.completedFuture(new ArrayList<>());
            }

            @Override
            public CompletableFuture<String> addProduct(Map<String, Object> product) {
                return CompletableFuture.completedFuture("mock-product-id");
            }

            @Override
            public CompletableFuture<Void> updateProduct(String id, Map<String, Object> data) {
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Void> deleteProduct(String id) {
                return CompletableFuture.completedFuture(null);
            }
        };
    }
}
