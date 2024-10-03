package org.ntm.practico1.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class Categoria {
    private String denominacion;
    @Id
    private String id;
    @ManyToMany
    private Collection<Articulo> articulos;
    private boolean eliminado;
}
