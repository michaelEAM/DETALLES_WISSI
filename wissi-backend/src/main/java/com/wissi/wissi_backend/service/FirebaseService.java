package com.wissi.wissi_backend.service;

import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class FirebaseService {

    @Autowired
    private DatabaseReference databaseReference;

    // Categorías
    public CompletableFuture<List<Map<String, Object>>> getCategories() {
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        
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
                future.completeExceptionally(error.toException());
            }
        });
        
        return future;
    }

    public CompletableFuture<String> addCategory(Map<String, Object> category) {
        CompletableFuture<String> future = new CompletableFuture<>();
        
        DatabaseReference newCategoryRef = databaseReference.child("categorias").push();
        newCategoryRef.setValue(category, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                future.completeExceptionally(databaseError.toException());
            } else {
                future.complete(newCategoryRef.getKey());
            }
        });
        
        return future;
    }

    public CompletableFuture<Void> updateCategory(String id, Map<String, Object> category) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        databaseReference.child("categorias").child(id).updateChildren(category, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                future.completeExceptionally(databaseError.toException());
            } else {
                future.complete(null);
            }
        });
        
        return future;
    }

    public CompletableFuture<Void> deleteCategory(String id) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        databaseReference.child("categorias").child(id).removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                future.completeExceptionally(databaseError.toException());
            } else {
                future.complete(null);
            }
        });
        
        return future;
    }

    // Productos
    public CompletableFuture<List<Map<String, Object>>> getProducts() {
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        
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
                future.completeExceptionally(error.toException());
            }
        });
        
        return future;
    }

    public CompletableFuture<String> addProduct(Map<String, Object> product) {
        CompletableFuture<String> future = new CompletableFuture<>();
        
        DatabaseReference newProductRef = databaseReference.child("productos").push();
        newProductRef.setValue(product, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                future.completeExceptionally(databaseError.toException());
            } else {
                future.complete(newProductRef.getKey());
            }
        });
        
        return future;
    }

    public CompletableFuture<Void> updateProduct(String id, Map<String, Object> product) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        databaseReference.child("productos").child(id).updateChildren(product, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                future.completeExceptionally(databaseError.toException());
            } else {
                future.complete(null);
            }
        });
        
        return future;
    }

    public CompletableFuture<Void> deleteProduct(String id) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        databaseReference.child("productos").child(id).removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                future.completeExceptionally(databaseError.toException());
            } else {
                future.complete(null);
            }
        });
        
        return future;
    }
}
