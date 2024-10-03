package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Inquilino;
import com.ntm.consorcio.domain.entity.Nacionalidad;
import com.ntm.consorcio.domain.entity.SexoEnum;
import com.ntm.consorcio.domain.entity.TipoDocumentoEnum;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOInquilinoBean;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 * Clase que implementa los métodos correspondientes a Inquilino
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class InquilinoServiceBean {
    private @EJB NacionalidadServiceBean nacionalidadServiceBean;
    private @EJB DAOInquilinoBean dao;
    
    /**
     * Crea el inquilino y llama al dao, quien lo persiste en la base de datos
     * @param nombre String
     * @param apellido String
     * @param telefono String
     * @param documento String
     * @param tipoDocumento TipoDocumentoEnum
     * @param correoElectronico String
     * @param sexo SexoEnum
     * @param fechaNacimiento Date
     * @param idNacionalidad String
     * @throws ErrorServiceException 
     */
    public void crearInquilino(String nombre, String apellido, String telefono, String documento, TipoDocumentoEnum tipoDocumento, String correoElectronico, SexoEnum sexo, Date fechaNacimiento, String idNacionalidad) throws ErrorServiceException {
                
        try {
            
            if (!verificar(documento)) {
                throw new ErrorServiceException("Debe indicar el documento");
            }
            
            try {
                dao.buscarInquilinoPorDocumento(documento);
                throw new ErrorServiceException("Existe un inquilino con ese documento");
            } catch (NoResultDAOException e) {
                
            }
            
            Nacionalidad nacionalidad = nacionalidadServiceBean.buscarNacionalidad(idNacionalidad);

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
            
            if (sexo == null) {
                throw new ErrorServiceException("Debe indicar el sexo");
            }
            
            if (tipoDocumento == null) {
                throw new ErrorServiceException("Debe indicar el tipo de documento");
            }
            
            if (fechaNacimiento == null) {
                throw new ErrorServiceException("Debe indicar la fecha de nacimiento");
            }
            
            Inquilino inquilino = new Inquilino();
            inquilino.setNombre(nombre);
            inquilino.setApellido(apellido);
            inquilino.setDocumento(documento);
            inquilino.setSexo(sexo);
            inquilino.setNacionalidad(nacionalidad);
            inquilino.setCorreoElectronico(correoElectronico);
            inquilino.setTelefono(telefono);
            inquilino.setTipoDocumento(tipoDocumento);
            inquilino.setEliminado(false);
            inquilino.setId(UUID.randomUUID().toString());
            inquilino.setFechaNacimiento(fechaNacimiento);
            
            dao.guardarInquilino(inquilino);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Modifica los atributos del objeto
     * @param idInquilino String
     * @param nombre String
     * @param apellido String
     * @param telefono String
     * @param documento String
     * @param tipoDocumento TipoDocumentoEnum
     * @param correoElectronico String
     * @param sexo SexoEnum
     * @param fechaNacimiento Date
     * @param idNacionalidad String
     * @throws ErrorServiceException 
     */
    public void modificarInquilino(String idInquilino, String nombre, String apellido, String telefono, String documento, TipoDocumentoEnum tipoDocumento, String correoElectronico, SexoEnum sexo, Date fechaNacimiento, String idNacionalidad) throws ErrorServiceException {

        try {

            Inquilino inquilino = buscarInquilino(idInquilino);

            Nacionalidad nacionalidad = nacionalidadServiceBean.buscarNacionalidad(idNacionalidad);

            if (!verificar(nombre)) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }
            
            if (!verificar(apellido)) {
                throw new ErrorServiceException("Debe indicar el apellido");
            }
            
            if (!verificar(telefono)) {
                throw new ErrorServiceException("Debe indicar el telefono");
            }
            
            if (!verificar(documento)) {
                throw new ErrorServiceException("Debe indicar el documento");
            }
            
            if (!verificar(correoElectronico)) {
                throw new ErrorServiceException("Debe indicar el correo electrónico");
            }
            
            if (sexo == null) {
                throw new ErrorServiceException("Debe indicar el sexo");
            }
            
            if (tipoDocumento == null) {
                throw new ErrorServiceException("Debe indicar el tipo de documento");
            }
            
            if (fechaNacimiento == null) {
                throw new ErrorServiceException("Debe indicar la fecha de nacimiento");
            }

            try {
                Inquilino inquilinoExistente = dao.buscarInquilinoPorDocumento(documento);
                if (!inquilinoExistente.getId().equals(idInquilino)){
                  throw new ErrorServiceException("Existe un inquilino con el documento indicado");  
                }
            } catch (NoResultDAOException ex) {
            }

            inquilino.setNombre(nombre);
            inquilino.setApellido(apellido);
            inquilino.setDocumento(documento);
            inquilino.setSexo(sexo);
            inquilino.setCorreoElectronico(correoElectronico);
            inquilino.setNacionalidad(nacionalidad);
            inquilino.setTelefono(telefono);
            inquilino.setTipoDocumento(tipoDocumento);
            inquilino.setEliminado(false);
            inquilino.setFechaNacimiento(fechaNacimiento);
            
            dao.actualizarInquilino(inquilino);

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
     * @return Objeto Inquilino
     * @throws ErrorServiceException 
     */
    public Inquilino buscarInquilino(String id) throws ErrorServiceException {

        try {
            
            if (id == null) {
                throw new ErrorServiceException("Debe indicar el inquilino");
            }

            Inquilino inquilino = dao.buscarInquilino(id);
            
            if (inquilino.isEliminado()){
                throw new ErrorServiceException("No se encuentra el inquilino indicado");
            }

            return inquilino;
            
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
    public void eliminarInquilino(String id) throws ErrorServiceException {

        try {

            Inquilino inquilino = buscarInquilino(id);
            inquilino.setEliminado(true);
            
            dao.actualizarInquilino(inquilino);

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
    public Collection<Inquilino> listarInquilinoActivo() throws ErrorServiceException {
        try {
            return dao.listarInquilinoActivo();

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
