package com.wissi.wissi_backend.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface FirebaseServiceInterface {

    // Categorías
    CompletableFuture<List<Map<String, Object>>> getCategories();

    CompletableFuture<String> addCategory(Map<String, Object> category);

    CompletableFuture<Void> updateCategory(String id, Map<String, Object> data);

    CompletableFuture<Void> deleteCategory(String id);


    // Productos
    CompletableFuture<List<Map<String, Object>>> getProducts();

    CompletableFuture<String> addProduct(Map<String, Object> product);

    CompletableFuture<Void> updateProduct(String id, Map<String, Object> data);

    CompletableFuture<Void> deleteProduct(String id);
}
