package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Pais;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOPaisBean;
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
public class PaisServiceBean {
    
    private DAOPaisBean dao;
    
    /**
     * Crea un objeto de la clase
     * @param nombre String con el nombre
     * @throws ErrorServiceException 
     */
    public void crearPais(String nombre) throws ErrorServiceException {

        try {
            
            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                dao.buscarPaisPorNombre(nombre);
                throw new ErrorServiceException("Existe un país con el nombre indicado");
            } catch (NoResultDAOException ex) {
               
            }

            Pais pais = new Pais();
            pais.setId(UUID.randomUUID().toString());
            pais.setNombre(nombre);
            pais.setEliminado(false);

            dao.guardarPais(pais);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Modifica los atributos del objeto
     * @param idPais String con el id
     * @param nombre String con el nombre
     * @throws ErrorServiceException 
     */
    public void modificarPais(String idPais, String nombre) throws ErrorServiceException {

        try {

            Pais pais = buscarPais(idPais);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try{
                Pais paisExistente = dao.buscarPaisPorNombre(nombre);
                if (!paisExistente.getId().equals(idPais)){
                  throw new ErrorServiceException("Existe un país con el nombre indicado");  
                }
            } catch (NoResultDAOException ex) {}

            pais.setNombre(nombre);
            
            dao.actualizarPais(pais);

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
     * @return Objeto país
     * @throws ErrorServiceException 
     */
    public Pais buscarPais(String id) throws ErrorServiceException {

        try {
            
            if (id == null) {
                throw new ErrorServiceException("Debe indicar el país");
            }

            Pais pais = dao.buscarPais(id);
            
            if (pais.getEliminado()){
                throw new ErrorServiceException("No se encuentra en país indicado");
            }

            return pais;
            
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
    public void eliminarPais(String id) throws ErrorServiceException {

        try {

            Pais pais = buscarPais(id);
            pais.setEliminado(true);
            
            dao.actualizarPais(pais);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }

    /**
     * Devuelve una lista con los objetos de la clase activos
     * @return Collection<Pais>
     * @throws ErrorServiceException 
     */
    public Collection<Pais> listarPaisActivo() throws ErrorServiceException {
        try {
            
            return dao.listarPaisActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}