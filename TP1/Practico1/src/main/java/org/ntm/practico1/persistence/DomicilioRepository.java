package org.ntm.practico1.persistence;

import org.ntm.practico1.domain.entity.Domicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomicilioRepository extends JpaRepository<Domicilio, String> {
    Domicilio findByNombreCalleAndNumeroAndEliminadoFalse(String nombre, int numero);
}
