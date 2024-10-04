package org.ntm.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
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
