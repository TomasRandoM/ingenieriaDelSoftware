package org.ntm.practico1.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Cliente {
    private String apellido;
    private int dni;
    @Id
    private String id;
    @OneToOne
    private Domicilio domicilio;
    private String nombre;
    private boolean eliminado;
}
