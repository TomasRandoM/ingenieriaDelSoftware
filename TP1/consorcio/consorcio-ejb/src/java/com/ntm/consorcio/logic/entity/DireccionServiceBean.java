package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Localidad;
import com.ntm.consorcio.domain.entity.Direccion;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAODireccionBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Clase que implementa los métodos correspondientes a la lógica de negocio
 * @author Tomas Rando
 * @version 1.0.0
 */
@Stateless
@LocalBean
public class DireccionServiceBean {
    
    private @EJB DAODireccionBean dao;
    private @EJB LocalidadServiceBean localidadService;
    
    /**
     * Crea un objeto de la clase
     * @param calle String con la calle
     * @param numeracion String con la numeración
     * @throws ErrorServiceException 
     */
    public void crearDireccion(String calle, String numeracion, String idLocalidad) throws ErrorServiceException {
        Localidad localidad;
        try {
            
            if (calle == null || calle.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la calle");
            }
            
            if (numeracion == null || numeracion.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la numeración");
            }

            /*
            try {
                dao.buscarDireccionPorNombre(nombre);
                throw new ErrorServiceException("Existe una direccion con el nombre indicado");
            } catch (NoResultDAOException ex) {
              
            }
            */
            try {
                localidad = localidadService.buscarLocalidad(idLocalidad);
            } catch (ErrorServiceException ex) {
                throw new ErrorServiceException("No se encontró la localidad seleccionada");
            }
            
            Direccion direccion = new Direccion();
            direccion.setId(UUID.randomUUID().toString());
            direccion.setNumeracion(numeracion);
            direccion.setCalle(calle);
            direccion.setEliminado(false);
            direccion.setLocalidad(localidad);

            dao.guardarDireccion(direccion);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Modifica los atributos del objeto
     * @param idDireccion String con el id
     * @param calle String con la calle
     * @param numeracion String con la numeración
     * @throws ErrorServiceException 
     */
    public void modificarDireccion(String idDireccion, String calle, String numeracion, String idLocalidad) throws ErrorServiceException {
        Localidad localidad;
        try {

            Direccion direccion = buscarDireccion(idDireccion);

            if (calle == null || calle.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la calle");
            }
            
            if (numeracion == null || numeracion.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la numeracion");
            }
            
            /*
            try{
                Direccion direccionExistente = dao.buscarDireccionPorNombre(nombre);
                if (!direccionExistente.getId().equals(idDireccion)){
                  throw new ErrorServiceException("Existe una direccion con el nombre indicado");  
                }
            } catch (NoResultDAOException ex) {}
            */
            
            try {
                localidad = localidadService.buscarLocalidad(idLocalidad);
            } catch (ErrorServiceException ex) {
                throw new ErrorServiceException("No se encontró la localidad seleccionada");
            }
            
            direccion.setCalle(calle);
            direccion.setNumeracion(numeracion);
            direccion.setLocalidad(localidad);
            
            dao.actualizarDireccion(direccion);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    /**
     * Busca el objeto y lo devuelve si lo encuentra
     * @param id String con el id
     * @return Objeto Direccion
     * @throws ErrorServiceException 
     */
    public Direccion buscarDireccion(String id) throws ErrorServiceException {

        try {
            
            if (id == null) {
                throw new ErrorServiceException("Debe indicar la direccion");
            }

            Direccion direccion = dao.buscarDireccion(id);
            
            if (direccion.getEliminado()){
                throw new ErrorServiceException("No se encuentra la direccion indicada");
            }

            return direccion;
            
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
    public void eliminarDireccion(String id) throws ErrorServiceException {

        try {

            Direccion direccion = buscarDireccion(id);
            direccion.setEliminado(true);
            
            dao.actualizarDireccion(direccion);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Retorna una Collection de las direcciones activas
     * @return Collection
     * @throws ErrorServiceException 
     */
    public Collection<Direccion> listarDireccionActivo() throws ErrorServiceException {
        try {
            return dao.listarDireccionActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Retorna una Collection con las direcciones activas de la localidad especificada
     * @param id String
     * @return Collection
     * @throws ErrorServiceException 
     */
    public Collection<Direccion> listarDireccionActivoPorLocalidad(String id) throws ErrorServiceException {
        try {
            return dao.listarDireccionActivoPorLocalidad(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}