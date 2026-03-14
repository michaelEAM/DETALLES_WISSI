package com.wissi.wissi_backend.controller;

import com.wissi.wissi_backend.service.FirebaseServiceInterface;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(
        origins = {
                "https://detalles-wissi.vercel.app",
                "http://localhost:3000"
        },
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        },
        allowedHeaders = "*"
)
public class CategoriaController {

    private final FirebaseServiceInterface firebaseService;

    public CategoriaController(FirebaseServiceInterface firebaseService) {
        this.firebaseService = firebaseService;
    }

    // Obtener todas las categorias
    @GetMapping
    public List<Map<String, Object>> getAll() throws ExecutionException, InterruptedException {
        return firebaseService.getCategories().get();
    }

    // Crear categoria
    @PostMapping
    public String create(@RequestBody Map<String, Object> categoria)
            throws ExecutionException, InterruptedException {

        return firebaseService.addCategory(categoria).get();
    }

    // Obtener categoria por id
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        List<Map<String, Object>> categories = firebaseService.getCategories().get();

        return categories.stream()
                .filter(cat -> id.equals(cat.get("id")))
                .findFirst()
                .orElse(null);
    }

    // Actualizar categoria
    @PutMapping("/{id}")
    public void update(@PathVariable String id,
                       @RequestBody Map<String, Object> data)
            throws ExecutionException, InterruptedException {

        firebaseService.updateCategory(id, data).get();
    }

    // Eliminar categoria
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id)
            throws ExecutionException, InterruptedException {

        firebaseService.deleteCategory(id).get();
    }

    // Manejar solicitudes OPTIONS (preflight)
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public void handleOptions() {
    }
}
