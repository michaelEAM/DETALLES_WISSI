package com.wissi.wissi_backend.service;

import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
public class FirebaseService {

    @Autowired(required = false)
    private DatabaseReference databaseReference;

    // Categorías
    public CompletableFuture<List<Map<String, Object>>> getCategories() {
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        
        if (databaseReference == null) {
            future.completeExceptionally(new RuntimeException("Firebase not configured"));
            return future;
        }
        
        databaseReference.child("categorias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Map<String, Object>> categories = new ArrayList<>();
                
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Map<String, Object> category = child.getValue(Map.class);
                        category.put("id", child.getKey());
                        categories.add(category);
                    }
                }
                
                future.complete(categories);
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        
        return future;
    }

    public CompletableFuture<String> addCategory(Map<String, Object> category) {
        CompletableFuture<String> future = new CompletableFuture<>();
        
        if (databaseReference == null) {
            future.completeExceptionally(new RuntimeException("Firebase not configured"));
            return future;
        }
        
        DatabaseReference newCategoryRef = databaseReference.child("categorias").push();
        
        Map<String, Object> categoryData = new HashMap<>();
        categoryData.put("nombre", category.get("nombre"));
        categoryData.put("descripcion", category.get("descripcion"));
        
        newCategoryRef.setValue(categoryData, (error, ref) -> {
            if (error != null) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            } else {
                future.complete(ref.getKey());
            }
        });
        
        return future;
    }

    public CompletableFuture<Void> updateCategory(String id, Map<String, Object> data) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        if (databaseReference == null) {
            future.completeExceptionally(new RuntimeException("Firebase not configured"));
            return future;
        }
        
        Map<String, Object> updateData = new HashMap<>();
        if (data.containsKey("nombre")) {
            updateData.put("nombre", data.get("nombre"));
        }
        if (data.containsKey("descripcion")) {
            updateData.put("descripcion", data.get("descripcion"));
        }
        
        databaseReference.child("categorias").child(id).updateChildren(updateData, (error, ref) -> {
            if (error != null) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            } else {
                future.complete(null);
            }
        });
        
        return future;
    }

    public CompletableFuture<Void> deleteCategory(String id) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        if (databaseReference == null) {
            future.completeExceptionally(new RuntimeException("Firebase not configured"));
            return future;
        }
        
        databaseReference.child("categorias").child(id).removeValue((error, ref) -> {
            if (error != null) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            } else {
                future.complete(null);
            }
        });
        
        return future;
    }

    // Productos
    public CompletableFuture<List<Map<String, Object>>> getProducts() {
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        
        if (databaseReference == null) {
            future.completeExceptionally(new RuntimeException("Firebase not configured"));
            return future;
        }
        
        databaseReference.child("productos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Map<String, Object>> products = new ArrayList<>();
                
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Map<String, Object> product = child.getValue(Map.class);
                        product.put("id", child.getKey());
                        products.add(product);
                    }
                }
                
                future.complete(products);
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        
        return future;
    }

    public CompletableFuture<String> addProduct(Map<String, Object> product) {
        CompletableFuture<String> future = new CompletableFuture<>();
        
        if (databaseReference == null) {
            future.completeExceptionally(new RuntimeException("Firebase not configured"));
            return future;
        }
        
        DatabaseReference newProductRef = databaseReference.child("productos").push();
        
        Map<String, Object> productData = new HashMap<>();
        productData.put("nombre", product.get("nombre"));
        productData.put("descripcion", product.get("descripcion"));
        productData.put("precio", product.get("precio"));
        productData.put("stock", product.get("stock"));
        productData.put("id_categoria", product.get("id_categoria"));
        productData.put("imagenUrl", product.get("imagenUrl"));
        
        newProductRef.setValue(productData, (error, ref) -> {
            if (error != null) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            } else {
                future.complete(ref.getKey());
            }
        });
        
        return future;
    }

    public CompletableFuture<Void> updateProduct(String id, Map<String, Object> data) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        if (databaseReference == null) {
            future.completeExceptionally(new RuntimeException("Firebase not configured"));
            return future;
        }
        
        Map<String, Object> updateData = new HashMap<>();
        if (data.containsKey("nombre")) {
            updateData.put("nombre", data.get("nombre"));
        }
        if (data.containsKey("descripcion")) {
            updateData.put("descripcion", data.get("descripcion"));
        }
        if (data.containsKey("precio")) {
            updateData.put("precio", data.get("precio"));
        }
        if (data.containsKey("stock")) {
            updateData.put("stock", data.get("stock"));
        }
        if (data.containsKey("id_categoria")) {
            updateData.put("id_categoria", data.get("id_categoria"));
        }
        if (data.containsKey("imagenUrl")) {
            updateData.put("imagenUrl", data.get("imagenUrl"));
        }
        
        databaseReference.child("productos").child(id).updateChildren(updateData, (error, ref) -> {
            if (error != null) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            } else {
                future.complete(null);
            }
        });
        
        return future;
    }

    public CompletableFuture<Void> deleteProduct(String id) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        if (databaseReference == null) {
            future.completeExceptionally(new RuntimeException("Firebase not configured"));
            return future;
        }
        
        databaseReference.child("productos").child(id).removeValue((error, ref) -> {
            if (error != null) {
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            } else {
                future.complete(null);
            }
        });
        
        return future;
    }
}
