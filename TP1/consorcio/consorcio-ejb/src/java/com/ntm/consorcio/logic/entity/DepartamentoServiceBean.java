package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Provincia;
import com.ntm.consorcio.domain.entity.Departamento;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAODepartamentoBean;
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
public class DepartamentoServiceBean {
    
    private @EJB DAODepartamentoBean dao;
    private @EJB ProvinciaServiceBean provinciaService;
    
    /**
     * Crea un objeto de la clase
     * @param nombre String con el nombre
     * @throws ErrorServiceException 
     */
    public void crearDepartamento(String nombre, String idProvincia) throws ErrorServiceException {
        Provincia provincia;
        try {
            
            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try {
                dao.buscarDepartamentoPorNombre(nombre);
                throw new ErrorServiceException("Existe un departamento con el nombre indicado");
            } catch (NoResultDAOException ex) {
            }
            
            try {
                provincia = provinciaService.buscarProvincia(idProvincia);
            } catch (ErrorServiceException ex) {
                throw new ErrorServiceException("No se encontró la provincia seleccionada");
            }

            Departamento departamento = new Departamento();
            departamento.setId(UUID.randomUUID().toString());
            departamento.setNombre(nombre);
            departamento.setEliminado(false);
            departamento.setProvincia(provincia);

            dao.guardarDepartamento(departamento);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Modifica los atributos del objeto
     * @param idDepartamento String con el id
     * @param nombre String con el nombre
     * @throws ErrorServiceException 
     */
    public void modificarDepartamento(String idDepartamento, String nombre, String idProvincia) throws ErrorServiceException {
        Provincia provincia;
        try {

            Departamento departamento = buscarDepartamento(idDepartamento);

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            try{
                Departamento departamentoExistente = dao.buscarDepartamentoPorNombre(nombre);
                if (!departamentoExistente.getId().equals(idDepartamento)){
                  throw new ErrorServiceException("Existe un departamento con el nombre indicado");  
                }
            } catch (NoResultDAOException ex) {}

            try {
                provincia = provinciaService.buscarProvincia(idProvincia);
            } catch (ErrorServiceException ex) {
                throw new ErrorServiceException("No se encontró la provincia seleccionada");
            }
            
            departamento.setNombre(nombre);
            departamento.setProvincia(provincia);
            dao.actualizarDepartamento(departamento);

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
     * @return Objeto Departamento
     * @throws ErrorServiceException 
     */
    public Departamento buscarDepartamento(String id) throws ErrorServiceException {

        try {
            
            if (id == null) {
                throw new ErrorServiceException("Debe indicar el departamento");
            }

            Departamento departamento = dao.buscarDepartamento(id);
            
            if (departamento.getEliminado()){
                throw new ErrorServiceException("No se encuentra el departamento indicado");
            }

            return departamento;
            
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
    public void eliminarDepartamento(String id) throws ErrorServiceException {

        try {

            Departamento departamento = buscarDepartamento(id);
            departamento.setEliminado(true);
            
            dao.actualizarDepartamento(departamento);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }

    /**
     * Devuelve una lista con los objetos de la clase activos
     * @return Collection<Departamento>
     * @throws ErrorServiceException 
     */
    public Collection<Departamento> listarDepartamentoActivo() throws ErrorServiceException {
        try {
            
            return dao.listarDepartamentoActivo();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Listar departamentos activos asociados a cierta provincia
     * @param id String
     * @return Collection
     * @throws ErrorServiceException 
     */
    public Collection<Departamento> listarDepartamentoActivoPorProvincia(String id) throws ErrorServiceException {
        try {
            if (id == null || id.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la provincia");
            }
            
            return dao.listarDepartamentoActivoPorProvincia(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
}