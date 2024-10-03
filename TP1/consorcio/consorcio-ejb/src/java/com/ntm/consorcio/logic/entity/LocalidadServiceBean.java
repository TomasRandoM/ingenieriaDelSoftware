package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Departamento;
import com.ntm.consorcio.domain.entity.Localidad;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOLocalidadBean;
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
public class LocalidadServiceBean {
    
    private @EJB DAOLocalidadBean dao;
    private @EJB DepartamentoServiceBean departamentoService;
    /**
     * Crea un objeto de la clase
     * @param nombre String con el nombre
     * @throws ErrorServiceException 
     */
    public void crearLocalidad(String nombre, String codigoPostal, String idDepartamento) throws ErrorServiceException {
        Departamento departamento;
        try {
            
            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                dao.buscarLocalidadPorNombre(nombre);
                throw new ErrorServiceException("Existe una localidad con el nombre indicado");
            } catch (NoResultDAOException ex) {
               
            }
            
            try {
                departamento = departamentoService.buscarDepartamento(idDepartamento);
            } catch (ErrorServiceException ex) {
                throw new ErrorServiceException("No se encontró el departamento seleccionado");
            }
            
            Localidad localidad = new Localidad();
            localidad.setId(UUID.randomUUID().toString());
            localidad.setNombre(nombre);
            localidad.setCodigoPostal(codigoPostal);
            localidad.setEliminado(false);
            localidad.setDepartamento(departamento);

            dao.guardarLocalidad(localidad);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Modifica los atributos del objeto
     * @param idLocalidad String con el id
     * @param nombre String con el nombre
     * @throws ErrorServiceException 
     */
    public void modificarLocalidad(String idLocalidad, String nombre, String codigoPostal, String idDepartamento) throws ErrorServiceException {
        Departamento departamento;
        try {

            Localidad localidad = buscarLocalidad(idLocalidad);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try{
                Localidad localidadExistente = dao.buscarLocalidadPorNombre(nombre);
                if (!localidadExistente.getId().equals(idLocalidad)){
                  throw new ErrorServiceException("Existe una localidad con el nombre indicado");  
                }
            } catch (NoResultDAOException ex) {}
            
            try {
                departamento = departamentoService.buscarDepartamento(idDepartamento);
            } catch (ErrorServiceException ex) {
                throw new ErrorServiceException("No se encontró el departamento seleccionado");
            }
            
            localidad.setNombre(nombre);
            localidad.setCodigoPostal(codigoPostal);
            localidad.setDepartamento(departamento);
            
            dao.actualizarLocalidad(localidad);

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
     * @return Objeto Localidad
     * @throws ErrorServiceException 
     */
    public Localidad buscarLocalidad(String id) throws ErrorServiceException {

        try {
            
            if (id == null) {
                throw new ErrorServiceException("Debe indicar la localidad");
            }

            Localidad localidad = dao.buscarLocalidad(id);
            
            if (localidad.getEliminado()){
                throw new ErrorServiceException("No se encuentra la localidad indicada");
            }

            return localidad;
            
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
    public void eliminarLocalidad(String id) throws ErrorServiceException {

        try {

            Localidad localidad = buscarLocalidad(id);
            localidad.setEliminado(true);
            
            dao.actualizarLocalidad(localidad);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }

    /**
     * Devuelve una lista con los objetos de la clase activos
     * @return Collection<Localidad>
     * @throws ErrorServiceException 
     */
    public Collection<Localidad> listarLocalidadActivo() throws ErrorServiceException {
        try {
            
            return dao.listarLocalidadActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Devuelve una lista con los objetos de la clase activos
     * @return Collection
     * @throws ErrorServiceException 
     */
    public Collection<Localidad> listarLocalidadActivoPorDepartamento(String id) throws ErrorServiceException {
        try {            
            if (id == null || id.isEmpty()) {
                throw new ErrorServiceException("No se encuentra el departamento indicado");
            }
            return dao.listarLocalidadActivoPorDepartamento(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}