package com.wissi.wissi_backend.controller;

import com.wissi.wissi_backend.service.FirebaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final FirebaseService firebaseService;

    public CategoriaController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @GetMapping
    public List<Map<String, Object>> getAll() throws ExecutionException, InterruptedException {
        return firebaseService.getCategories().get();
    }

    @PostMapping
    public String create(@RequestBody Map<String, Object> categoria) throws ExecutionException, InterruptedException {
        return firebaseService.addCategory(categoria).get();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable String id) throws ExecutionException, InterruptedException {
        List<Map<String, Object>> categories = firebaseService.getCategories().get();
        return categories.stream()
                .filter(cat -> id.equals(cat.get("id")))
                .findFirst()
                .orElse(null);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable String id, @RequestBody Map<String, Object> data) throws ExecutionException, InterruptedException {
        firebaseService.updateCategory(id, data).get();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws ExecutionException, InterruptedException {
        firebaseService.deleteCategory(id).get();
    }
}
