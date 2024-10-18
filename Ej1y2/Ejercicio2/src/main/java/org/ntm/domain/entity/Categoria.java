package org.ntm.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
