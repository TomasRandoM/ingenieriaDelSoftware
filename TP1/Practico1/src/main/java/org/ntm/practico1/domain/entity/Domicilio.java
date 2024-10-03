package org.ntm.practico1.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Domicilio {
    @Id
    private String id;
    private String nombreCalle;
    private int numero;
    private boolean eliminado;
}
