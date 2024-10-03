package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Perfil;
import com.ntm.consorcio.domain.entity.Usuario;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.logic.ErrorServiceLoginException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOUsuarioBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 * Clase que implementa los métodos de Usuario
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class UsuarioServiceBean {
    private @EJB SecurityServiceBean securityService;
    private @EJB DAOUsuarioBean dao;
    private @EJB PerfilServiceBean perfilServiceBean;
    
    public Usuario crearUsuario(String nombre, String apellido, String telefono, String correoElectronico, String usuario2, String clave, String claveVerific) throws ErrorServiceException {
        try {
            
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
            
            if (!verificar(usuario2)) {
                throw new ErrorServiceException("Debe indicar el usuario");
            }
            
            if (!verificar(clave)) {
                throw new ErrorServiceException("Debe indicar la clave");
            }
            
            if (!claveVerific.equals(clave)) {
                throw new ErrorServiceException("Las claves deben coincidir. Verificar");
            }
            try {
                dao.buscarUsuarioPorUsuario(usuario2);
                throw new ErrorServiceException("Ya existe el usuario especificado");
            } catch (NoResultDAOException e) {
            }
            
            Usuario usuario = new Usuario();
            usuario.setUsuario(usuario2);
            usuario.setNombre(nombre);
            usuario.setClave(securityService.hashClave(clave));
            usuario.setApellido(apellido);
            usuario.setTelefono(telefono);
            usuario.setCorreoElectronico(correoElectronico);
            usuario.setEliminado(false);
            usuario.setId(UUID.randomUUID().toString());
            
            dao.guardarUsuario(usuario);
            return usuario;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Modifica el usuario
     * @param idUsuario String
     * @param nombre String
     * @param apellido String
     * @param telefono String
     * @param correoElectronico String
     * @param usuario2 String
     * @param clave String
     * @throws ErrorServiceException 
     */
    public void modificarUsuario(String idUsuario, String nombre, String apellido, String telefono, String correoElectronico, String usuario2) throws ErrorServiceException {

        try {
            
            Usuario usuario = buscarUsuario(idUsuario);
            
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
            
            if (!verificar(usuario2)) {
                throw new ErrorServiceException("Debe indicar el usuario");
            }

            usuario.setUsuario(usuario2);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setTelefono(telefono);
            usuario.setCorreoElectronico(correoElectronico);
            
            
            dao.actualizarUsuario(usuario);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Modifica la clave
     * @param idUsuario String
     * @param clave String
     * @param claveVerific String
     * @throws ErrorServiceException 
     */
    public void modificarClave(String idUsuario, String clave, String claveVerific) throws ErrorServiceException {

        try {

            Usuario usuario = buscarUsuario(idUsuario);

            if (!verificar(clave)) {
                throw new ErrorServiceException("Debe indicar la clave");
            }
            
            if (!clave.equals(claveVerific)) {
                throw new ErrorServiceException("Las claves no coinciden");
            }

            usuario.setClave(securityService.hashClave(clave));
            
            
            dao.actualizarUsuario(usuario);

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
     * @return Objeto Usuario
     * @throws ErrorServiceException 
     */
    public Usuario buscarUsuario(String id) throws ErrorServiceException {

        try {
            
            if (!verificar(id)) {

                throw new ErrorServiceException("Debe indicar el usuario");
            }

            Usuario usuario = dao.buscarUsuario(id);
            
            if (usuario.isEliminado()){
                throw new ErrorServiceException("No se encuentra el usuario indicado");
            }

            return usuario;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
        public Usuario buscarUsuarioPorUsuario(String user) throws ErrorServiceException {

        try {
            
            if (user == null) {
                throw new ErrorServiceException("Debe indicar el usuario");
            }

            Usuario usuario = dao.buscarUsuarioPorUsuario(user);
            
            if (usuario.isEliminado()){
                throw new ErrorServiceException("No se encuentra el usuario indicado");
            }

            return usuario;
            
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
    public void eliminarUsuario(String id) throws ErrorServiceException {

        try {

            Usuario usuario = buscarUsuario(id);
            usuario.setEliminado(true);
            
            dao.actualizarUsuario(usuario);

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
    public Collection<Usuario> listarUsuarioActivo() throws ErrorServiceException {
        try {
            return dao.listarUsuarioActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Loguea al usuario
     * @param user String
     * @param clave String
     * @return Usuario
     * @throws ErrorServiceException 
     */
    public Usuario login(String user, String clave) throws ErrorServiceException, ErrorServiceLoginException {
        try {
            Usuario usuario = null;
            if (!verificar(user)) {
                throw new ErrorServiceException("Debe indicar el usuario");
            }

            if (!verificar(clave)) {
                throw new ErrorServiceException("Debe indicar la clave");
            }

            try {
                usuario = dao.buscarUsuarioPorUsuarioClave(user, securityService.hashClave(clave));
            } catch (NoResultDAOException e) {
                throw new ErrorServiceLoginException("Usuario o contraseña incorrectos");
            }
            
            if (usuario.getPerfil() == null) {
                throw new ErrorServiceException("No tiene perfil asignado");
            }
            return usuario;
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (ErrorServiceLoginException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }    
    }
    
    /**
     * Asigna el perfil al usuario
     * @param idUsuario String
     * @param idPerfil String
     * @throws ErrorServiceException 
     */
    public void asignarPerfil(String idUsuario, String idPerfil) throws ErrorServiceException {
        try {
            if (!verificar(idUsuario)) {
                    throw new ErrorServiceException("Debe indicar el usuario");
            }

            if (!verificar(idPerfil)) {
                    throw new ErrorServiceException("Debe indicar el perfil");
            }
            Usuario usuario = buscarUsuario(idUsuario);
            Perfil perfil = perfilServiceBean.buscarPerfil(idPerfil);
            usuario.setPerfil(perfil);
            
            dao.actualizarUsuario(usuario);
        } catch (ErrorServiceException ex) {
            throw ex;
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
