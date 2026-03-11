package com.wissi.wissi_backend.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service("mockFirebaseService")
public class MockFirebaseService {

    // Base de datos en memoria
    private final Map<String, Map<String, Object>> categories = new HashMap<>();
    private final Map<String, Map<String, Object>> products = new HashMap<>();
    private final Map<String, Map<String, Object>> users = new HashMap<>();

    public MockFirebaseService() {
        System.out.println("🔥 Mock Firebase Service initialized - Using in-memory database");
        
        // Datos de ejemplo
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Categorías de ejemplo
        Map<String, Object> cat1 = new HashMap<>();
        cat1.put("id", "cat1");
        cat1.put("nombre", "Peluches");
        cat1.put("descripcion", "Peluches de diferentes tamaños");
        categories.put("cat1", cat1);

        Map<String, Object> cat2 = new HashMap<>();
        cat2.put("id", "cat2");
        cat2.put("nombre", "Juguetes Educativos");
        cat2.put("descripcion", "Juguetes para aprendizaje");
        categories.put("cat2", cat2);

        // Productos de ejemplo
        Map<String, Object> prod1 = new HashMap<>();
        prod1.put("id", "prod1");
        prod1.put("nombre", "Oso de Peluche Grande");
        prod1.put("descripcion", "Oso suave de 50cm");
        prod1.put("precio", 29.99);
        prod1.put("stock", 10);
        prod1.put("id_categoria", "cat1");
        prod1.put("imagenUrl", "https://via.placeholder.com/300x200");
        products.put("prod1", prod1);

        Map<String, Object> prod2 = new HashMap<>();
        prod2.put("id", "prod2");
        prod2.put("nombre", "Rompecabezas 100 piezas");
        prod2.put("descripcion", "Rompecabezas educativo");
        prod2.put("precio", 15.99);
        prod2.put("stock", 25);
        prod2.put("id_categoria", "cat2");
        prod2.put("imagenUrl", "https://via.placeholder.com/300x200");
        products.put("prod2", prod2);

        System.out.println("📊 Sample data loaded: " + categories.size() + " categories, " + products.size() + " products");
    }

    // ========== CATEGORÍAS ==========
    public CompletableFuture<List<Map<String, Object>>> getCategories() {
        return CompletableFuture.completedFuture(new ArrayList<>(categories.values()));
    }

    public CompletableFuture<String> addCategory(Map<String, Object> category) {
        String id = "cat" + (categories.size() + 1);
        category.put("id", id);
        categories.put(id, new HashMap<>(category));
        System.out.println("✅ Category added: " + category.get("nombre"));
        return CompletableFuture.completedFuture(id);
    }

    public CompletableFuture<Void> updateCategory(String id, Map<String, Object> data) {
        if (categories.containsKey(id)) {
            Map<String, Object> existing = categories.get(id);
            existing.putAll(data);
            System.out.println("✅ Category updated: " + id);
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> deleteCategory(String id) {
        if (categories.remove(id) != null) {
            System.out.println("✅ Category deleted: " + id);
        }
        return CompletableFuture.completedFuture(null);
    }

    // ========== PRODUCTOS ==========
    public CompletableFuture<List<Map<String, Object>>> getProducts() {
        return CompletableFuture.completedFuture(new ArrayList<>(products.values()));
    }

    public CompletableFuture<String> addProduct(Map<String, Object> product) {
        String id = "prod" + (products.size() + 1);
        product.put("id", id);
        products.put(id, new HashMap<>(product));
        System.out.println("✅ Product added: " + product.get("nombre"));
        return CompletableFuture.completedFuture(id);
    }

    public CompletableFuture<Void> updateProduct(String id, Map<String, Object> data) {
        if (products.containsKey(id)) {
            Map<String, Object> existing = products.get(id);
            existing.putAll(data);
            System.out.println("✅ Product updated: " + id);
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> deleteProduct(String id) {
        if (products.remove(id) != null) {
            System.out.println("✅ Product deleted: " + id);
        }
        return CompletableFuture.completedFuture(null);
    }

    // ========== UTILIDADES ==========
    public void printDatabaseState() {
        System.out.println("\n📊 DATABASE STATE:");
        System.out.println("Categories: " + categories.size());
        System.out.println("Products: " + products.size());
        System.out.println("Users: " + users.size());
        System.out.println();
    }
}
