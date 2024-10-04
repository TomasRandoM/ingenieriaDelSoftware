package org.ntm.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
