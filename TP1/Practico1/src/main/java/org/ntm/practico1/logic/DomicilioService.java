package org.ntm.practico1.logic;

import org.ntm.practico1.domain.entity.Domicilio;
import org.ntm.practico1.persistence.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DomicilioService {

    @Autowired
    private DomicilioRepository repositorio;

    public void crearDomicilio(String nombreCalle, int numero) throws ErrorServiceException {
     try {
         if (nombreCalle.isEmpty() || nombreCalle == null) {
             throw new ErrorServiceException("Debe indicar el nombre de la calle");
         }
         if (numero < 0) {
             throw new ErrorServiceException("Numeración incorrecta");
         }

         if (repositorio.findByNombreCalleAndNumeroAndEliminadoFalse(nombreCalle, numero) != null) {
             throw new ErrorServiceException("El domicilio ya existe");
         }

         Domicilio domicilio = new Domicilio();
         domicilio.setId(UUID.randomUUID().toString());
         domicilio.setNumero(numero);
         domicilio.setEliminado(false);
         domicilio.setNombreCalle(nombreCalle);

         repositorio.save(domicilio);
     } catch (ErrorServiceException e) {
         throw e;
     } catch (Exception e) {
         throw new ErrorServiceException("Error de sistemas");
     }
    }
}
