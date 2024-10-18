package org.ntm.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

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
