package org.ntm.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
