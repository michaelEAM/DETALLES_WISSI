    package com.wissi.wissi_backend.model;

    import com.fasterxml.jackson.annotation.JsonProperty;
    import jakarta.persistence.*;
    import lombok.Data;

    @Entity
    @Data
    @Table(name = "categorias")
    public class Categoria {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idCategoria;

        @Column(nullable = false)
        private String nombre;

        private String descripcion;

        // Este getter hace que el JSON tenga "id"
        @JsonProperty("id")
        public Long getIdCategoria() {
            return idCategoria;
        }

    }
