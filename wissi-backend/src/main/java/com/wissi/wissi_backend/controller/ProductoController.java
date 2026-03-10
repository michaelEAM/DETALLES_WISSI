package com.wissi.wissi_backend.controller;

import com.wissi.wissi_backend.service.FirebaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final FirebaseService firebaseService;

    public ProductoController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @GetMapping
    public List<Map<String, Object>> getAll() throws ExecutionException, InterruptedException {
        return firebaseService.getProducts().get();
    }

    @PostMapping
    public String create(@RequestBody Map<String, Object> producto) throws ExecutionException, InterruptedException {
        return firebaseService.addProduct(producto).get();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable String id) throws ExecutionException, InterruptedException {
        List<Map<String, Object>> products = firebaseService.getProducts().get();
        return products.stream()
                .filter(product -> id.equals(product.get("id")))
                .findFirst()
                .orElse(null);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable String id, @RequestBody Map<String, Object> data) throws ExecutionException, InterruptedException {
        firebaseService.updateProduct(id, data).get();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws ExecutionException, InterruptedException {
        firebaseService.deleteProduct(id).get();
    }
}
