package org.ntm.practico1.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Articulo {
    private int cantidad;
    private String denominacion;
    private int precio;
    @Id
    private String id;
    private boolean eliminado;
}
