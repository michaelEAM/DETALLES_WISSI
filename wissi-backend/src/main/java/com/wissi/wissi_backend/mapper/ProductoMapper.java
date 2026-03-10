package com.wissi.wissi_backend.mapper;

import com.wissi.wissi_backend.dto.ProductoDTO;
import com.wissi.wissi_backend.model.Producto;
import com.wissi.wissi_backend.service.CategoriaService;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    private final CategoriaService categoriaService;

    public ProductoMapper(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public Producto toEntity(ProductoDTO dto) {
        if (dto == null) {
            return null;
        }

        Producto producto = new Producto();
        producto.setIdProducto(dto.getIdProducto());
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setStock(dto.getStock());
        
        // Set the category by ID
        if (dto.getCategoriaId() != null) {
            producto.setCategoria(categoriaService.obtenerCategoriaPorId(dto.getCategoriaId()));
        }
        
        return producto;
    }

    public ProductoDTO toDto(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setCodigo(producto.getCodigo());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.setStock(producto.getStock());
        
        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getIdCategoria());
        }
        
        return dto;
    }
}
