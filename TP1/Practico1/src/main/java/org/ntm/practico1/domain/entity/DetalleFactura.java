package org.ntm.practico1.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class DetalleFactura {
    private int cantidad;
    @Id
    private String id;
    private int subtotal;
    @ManyToOne
    private Articulo articulo;
    private boolean eliminado;
}
