package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Direccion;
import com.ntm.consorcio.domain.entity.Propietario;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOPropietarioBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;


/**
 * Clase que implementa los métodos correspondientes a Propietario
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class PropietarioServiceBean {
    private @EJB DAOPropietarioBean dao;
    private @EJB DireccionServiceBean serviceBean;
    
    /**
     * Crea un propietario y llama al dao para persistirlo en la base de datos
     * @param nombre String
     * @param apellido String
     * @param telefono String
     * @param correoElectronico String
     * @param habitaConsorcio boolean
     * @param idDireccion String
     * @throws ErrorServiceException 
     */
    public void crearPropietario(String nombre, String apellido, String telefono, String correoElectronico, boolean habitaConsorcio, String idDireccion) throws ErrorServiceException {
                
        try {
            
            Direccion direccion;
            
            if (idDireccion == null) {
                direccion = null;
            } else {
                direccion = serviceBean.buscarDireccion(idDireccion);
            }
 
            if (!verificar(nombre)) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }
            
            if (!verificar(apellido)) {
                throw new ErrorServiceException("Debe indicar el apellido");
            }
            
            if (!verificar(telefono)) {
                throw new ErrorServiceException("Debe indicar el telefono");
            }
            
            if (!verificar(correoElectronico)) {
                throw new ErrorServiceException("Debe indicar el correo electrónico");
            }
            
            try {
                dao.buscarPropietarioPorNombre(nombre, apellido);
                throw new ErrorServiceException("Existe un propietario con ese nombre");
            } catch (NoResultDAOException e) {
            }
            
            if (habitaConsorcio == false && direccion == null) {
                throw new ErrorServiceException("Debe indicar la dirección del propietario ya que no vive en el inmueble");
            }
            
            Propietario propietario = new Propietario();
            propietario.setNombre(nombre);
            propietario.setApellido(apellido);
            propietario.setTelefono(telefono);
            propietario.setDireccion(direccion);
            propietario.setCorreoElectronico(correoElectronico);
            propietario.setHabitaConsorcio(habitaConsorcio);
            propietario.setEliminado(false);
            propietario.setId(UUID.randomUUID().toString());
            
            dao.guardarPropietario(propietario);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Modifica el objeto
     * @param idPropietario String
     * @param nombre String
     * @param apellido String
     * @param telefono String
     * @param correoElectronico String
     * @param habitaConsorcio boolean
     * @param idDireccion String
     * @throws ErrorServiceException 
     */
    public void modificarPropietario(String idPropietario, String nombre, String apellido, String telefono, String correoElectronico, boolean habitaConsorcio, String idDireccion) throws ErrorServiceException {

        try {

            Propietario propietario = buscarPropietario(idPropietario);
            
            Direccion direccion;
            
            if (idDireccion == null) {
                direccion = null;
            } else {
                direccion = serviceBean.buscarDireccion(idDireccion);
            }

            if (!verificar(nombre)) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }
            
            if (!verificar(apellido)) {
                throw new ErrorServiceException("Debe indicar el apellido");
            }
            
            if (!verificar(telefono)) {
                throw new ErrorServiceException("Debe indicar el telefono");
            }
            
            if (!verificar(correoElectronico)) {
                throw new ErrorServiceException("Debe indicar el correo electrónico");
            }
            
            if (habitaConsorcio == false && direccion == null) {
                throw new ErrorServiceException("Debe indicar la dirección del propietario ya que no vive en el inmueble");
            }

            try {
                Propietario propietarioExistente = dao.buscarPropietarioPorNombre(nombre, apellido);
                if (!propietarioExistente.getId().equals(idPropietario)){
                  throw new ErrorServiceException("Existe un propietario con el nombre indicado");  
                }
            } catch (NoResultDAOException ex) {
            }

            propietario.setNombre(nombre);
            propietario.setApellido(apellido);
            propietario.setTelefono(telefono);
            propietario.setDireccion(direccion);
            propietario.setCorreoElectronico(correoElectronico);
            propietario.setHabitaConsorcio(habitaConsorcio);
            
            dao.actualizarPropietario(propietario);

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
     * @return Objeto Propietario
     * @throws ErrorServiceException 
     */
    public Propietario buscarPropietario(String id) throws ErrorServiceException {

        try {
            
            if (!verificar(id)) {
                throw new ErrorServiceException("Debe indicar el propietario");
            }

            Propietario propietario = dao.buscarPropietario(id);
            
            if (propietario.isEliminado()){
                throw new ErrorServiceException("No se encuentra el propietario indicado");
            }

            return propietario;
            
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
    public void eliminarPropietario(String id) throws ErrorServiceException {

        try {

            Propietario propietario = buscarPropietario(id);
            propietario.setEliminado(true);
            
            dao.actualizarPropietario(propietario);

        } catch (ErrorServiceException ex) {
            throw ex;
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
    public Collection<Propietario> listarPropietarioActivo() throws ErrorServiceException {
        try {
            return dao.listarPropietarioActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Verifica que la String no sea vacía o nula.
     * @param st String
     * @return boolean
     */
    public boolean verificar(String st) {
        if (st.isEmpty() || st == null) {
            return false;
        }
        return true;
    }
    
}
