package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Nacionalidad;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAONacionalidadBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 * Clase que implementa los métodos de Nacionalidad
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class NacionalidadServiceBean {
    private @EJB DAONacionalidadBean daoNacionalidadBean;
    
    /**
     * Crea la nacionalidad y llama al dao para persistirla en la base de datos
     * @param nombre String
     * @throws ErrorServiceException 
     */
    public void crearNacionalidad(String nombre) throws ErrorServiceException {
        try {
            if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServiceException("Debe indicar el nombre");
            }
            
            try {
                daoNacionalidadBean.buscarNacionalidadPorNombre(nombre);
                throw new ErrorServiceException("Existe un país con ese nombre");
            } catch (NoResultDAOException e) {
                
            }
            Nacionalidad nacionalidad = new Nacionalidad();
            nacionalidad.setNombre(nombre);
            nacionalidad.setEliminado(false);
            nacionalidad.setId(UUID.randomUUID().toString());
            
            daoNacionalidadBean.guardarNacionalidad(nacionalidad);
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
        
    }
    
    /**
     * Modifica la nacionalidad con el id ingresado
     * @param idNacionalidad String
     * @param nombre String
     * @throws ErrorServiceException 
     */
    public void modificarNacionalidad(String idNacionalidad, String nombre) throws ErrorServiceException {

        try {

            Nacionalidad nacionalidad = buscarNacionalidad(idNacionalidad);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try{
                Nacionalidad nacionalidadExistente = daoNacionalidadBean.buscarNacionalidadPorNombre(nombre);
                if (!nacionalidadExistente.getId().equals(idNacionalidad)){
                  throw new ErrorServiceException("Existe una nacionalidad con el nombre indicado");  
                }
            } catch (NoResultDAOException ex) {}

            nacionalidad.setNombre(nombre);
            
            daoNacionalidadBean.actualizarNacionalidad(nacionalidad);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Busca la nacionalidad con el id ingresado y la retorna
     * @param idNacionalidad String
     * @return Nacionalidad
     * @throws ErrorServiceException 
     */
    public Nacionalidad buscarNacionalidad(String idNacionalidad) throws ErrorServiceException {
        try {
            if (idNacionalidad == null) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            
            Nacionalidad nacionalidad = daoNacionalidadBean.buscarNacionalidad(idNacionalidad);
            
            if (nacionalidad.isEliminado()) {
                throw new ErrorServiceException("La nacionalidad solicitada no existe");
            }
            
            return nacionalidad;
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Elimina el objeto
     * @param id String que representa el id
     * @throws ErrorServiceException 
     */
    public void eliminarNacionalidad(String id) throws ErrorServiceException {

        try {

            Nacionalidad nacionalidad = buscarNacionalidad(id);
            nacionalidad.setEliminado(true);
            
            daoNacionalidadBean.actualizarNacionalidad(nacionalidad);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }
    
    /**
     * Lista las nacionalidades en el sistema. Devuelve un collection
     * @return Collection
     * @throws ErrorServiceException 
     */
    public Collection<Nacionalidad> listarNacionalidad() throws ErrorServiceException {
        try {
            return daoNacionalidadBean.listarNacionalidadActivo();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}
