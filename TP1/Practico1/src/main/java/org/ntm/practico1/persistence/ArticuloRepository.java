package org.ntm.practico1.persistence;

import org.ntm.practico1.domain.entity.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, String> {
}
