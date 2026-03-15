package com.wissi.wissi_backend.controller;

import com.wissi.wissi_backend.service.FirebaseServiceInterface;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = {"https://detalles-wissi.vercel.app", "http://localhost:3000"})
public class ProductoController {

    private final FirebaseServiceInterface firebaseService;

    public ProductoController(FirebaseServiceInterface firebaseService) {
        this.firebaseService = firebaseService;
    }

    // Obtener todos los productos
    @GetMapping
    public List<Map<String, Object>> getAll() throws ExecutionException, InterruptedException {
        return firebaseService.getProducts().get();
    }

    // Crear producto
    @PostMapping
    public String create(@RequestBody Map<String, Object> producto)
            throws ExecutionException, InterruptedException {

        return firebaseService.addProduct(producto).get();
    }

    // Obtener producto por id
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        List<Map<String, Object>> products = firebaseService.getProducts().get();

        return products.stream()
                .filter(product -> id.equals(product.get("id")))
                .findFirst()
                .orElse(null);
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public void update(@PathVariable String id,
                       @RequestBody Map<String, Object> data)
            throws ExecutionException, InterruptedException {

        firebaseService.updateProduct(id, data).get();
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        firebaseService.deleteProduct(id).get();
    }
}
