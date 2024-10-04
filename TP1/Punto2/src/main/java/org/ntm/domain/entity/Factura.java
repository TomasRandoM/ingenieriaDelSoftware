package org.ntm.domain.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Factura {
    private String fecha;
    @Id
    private String id;
    private int numero;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<DetalleFactura> detalleFacturas;
    private int total;
    @ManyToOne
    private Cliente cliente;
    private boolean eliminado;
}
