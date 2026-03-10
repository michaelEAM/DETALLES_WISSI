package com.wissi.wissi_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wissi.wissi_backend.dto.ProductoDTO;
import com.wissi.wissi_backend.mapper.ProductoMapper;
import com.wissi.wissi_backend.model.Producto;
import com.wissi.wissi_backend.repository.ProductoRepository;
import com.wissi.wissi_backend.service.CategoriaService;
import com.wissi.wissi_backend.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final ProductoMapper productoMapper;
    private final FileStorageService fileStorageService;

    public ProductoController(ProductoRepository productoRepository, 
                            CategoriaService categoriaService,
                            ProductoMapper productoMapper,
                            FileStorageService fileStorageService) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
        this.productoMapper = productoMapper;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAll() {
        List<ProductoDTO> productos = productoRepository.findAll().stream()
                .map(productoMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productos);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoDTO> create(
            @RequestPart("producto") String productoStr,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        
        try {
            // Convertir string JSON a objeto
            ObjectMapper objectMapper = new ObjectMapper();
            ProductoDTO productoDTO = objectMapper.readValue(productoStr, ProductoDTO.class);


            // 🔹 VALIDACIÓN: categoriaId no puede ser null
            if (productoDTO.getCategoriaId() == null) {
                return ResponseEntity.badRequest().build();
            }

            // 🔍 DEBUG 1: verificar si el DTO trae la categoria
            System.out.println("Categoria ID DTO: " + productoDTO.getCategoriaId());


            // Guardar la imagen si se proporcionó
            if (imagen != null && !imagen.isEmpty()) {
                String fileName = fileStorageService.storeFile(imagen);
                productoDTO.setImagenUrl("/imagenes/" + fileName);
            }
            
            // Guardar el producto
            Producto producto = productoMapper.toEntity(productoDTO);
            producto.setCategoria(
                    categoriaService.obtenerCategoriaPorId(productoDTO.getCategoriaId())
            );

            System.out.println("Categoria ENTITY: " + producto.getCategoria());

            Producto productoGuardado = productoRepository.save(producto);
            
            return ResponseEntity
                    .created(URI.create("/api/productos/" + productoGuardado.getIdProducto()))
                    .body(productoMapper.toDto(productoGuardado));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getById(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(productoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestPart("producto") String productoStr,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        
        return productoRepository.findById(id)
            .map(productoExistente -> {
                try {
                    // Convertir JSON a DTO
                    ObjectMapper objectMapper = new ObjectMapper();
                    ProductoDTO productoDTO = objectMapper.readValue(productoStr, ProductoDTO.class);
                    
                    // Actualizar campos básicos
                    productoExistente.setCodigo(productoDTO.getCodigo());
                    productoExistente.setNombre(productoDTO.getNombre());
                    productoExistente.setDescripcion(productoDTO.getDescripcion());
                    productoExistente.setPrecio(productoDTO.getPrecio());
                    productoExistente.setStock(productoDTO.getStock());
                    
                    // Actualizar categoría si es necesario
                    if (productoDTO.getCategoriaId() != null) {
                        productoExistente.setCategoria(
                            categoriaService.obtenerCategoriaPorId(productoDTO.getCategoriaId())
                        );
                    }
                    
                    // Actualizar imagen si se proporcionó una nueva
                    if (imagen != null && !imagen.isEmpty()) {
                        try {
                            String fileName = fileStorageService.storeFile(imagen);
                            productoExistente.setImagenUrl("/imagenes/" + fileName);
                        } catch (IOException e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error al guardar la imagen: " + e.getMessage());
                        }
                    }
                    
                    // Guardar cambios
                    Producto productoActualizado = productoRepository.save(productoExistente);
                    return ResponseEntity.ok(productoMapper.toDto(productoActualizado));
                    
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al actualizar el producto: " + e.getMessage());
                }
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productoRepository.deleteById(id);
    }
}
