
package com.ntm.consorcio.logic.entity;
import com.ntm.consorcio.domain.entity.EstadoInmueble;
import com.ntm.consorcio.domain.entity.Inmueble;
import com.ntm.consorcio.domain.entity.Inquilino;
import com.ntm.consorcio.domain.entity.Propietario;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOInmuebleBean;
import java.util.Collection;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author Martinotebook
 */
@Stateless
@LocalBean
public class InmuebleServiceBean {
    
    private @EJB DAOInmuebleBean dao;
    private @EJB PropietarioServiceBean propietarioService;
    private @EJB InquilinoServiceBean inquilinoService;
    
    /**
     * Crea un objeto Inmueble.
     * @param piso String con el número de piso.
     * @param dpto String con el número de departamento.
     * @param estadoInmueble Estado del inmueble.
     * @throws ErrorServiceException
     */
    public void crearInmueble(String piso, String dpto, EstadoInmueble estadoInmueble, String idPropietario, String idInquilino) throws ErrorServiceException {
        try {
            Propietario propietario;
            Inquilino inquilino;
            if (piso == null || piso.isEmpty() || dpto == null || dpto.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el piso y el departamento.");
            }
               
            // Verificar si ya existe un inmueble con el mismo piso y departamento.
            try {
                dao.buscarInmueblePorPisoYDpto(piso, dpto);
                throw new ErrorServiceException("Ya existe un inmueble con el mismo piso y departamento.");
            } catch (NoResultDAOException ex) {
                // No existe, lo creamos.
            }
            
            try {
                propietario = propietarioService.buscarPropietario(idPropietario);
            } catch (ErrorServiceException ex) {
                throw new ErrorServiceException("No se encontró el propietario seleccionado");
            }
            
            if (propietario.getHabitaConsorcio() == false && idInquilino == null && estadoInmueble == EstadoInmueble.OCUPADO) {
                throw new ErrorServiceException("Se encontraron incongruencias entre propietario/inquilino y el estado del inmueble");
            }
            
            if (propietario.getHabitaConsorcio() == true && estadoInmueble == EstadoInmueble.DESOCUPADO) {
                throw new ErrorServiceException("Se encontraron incongruencias entre propietario y el estado del inmueble");
            }
            
            if (idInquilino != null && estadoInmueble == EstadoInmueble.DESOCUPADO) {
                throw new ErrorServiceException("Se encontraron incongruencias entre inquilino y el estado del inmueble");
            }
            
            if (propietario.getHabitaConsorcio() || estadoInmueble == EstadoInmueble.DESOCUPADO) {
                inquilino = null;
            } else {
                try {
                    if (idInquilino == null || idInquilino.isEmpty()) {
                        throw new ErrorServiceException("No se encontró el inquilino");
                    }
                    inquilino = inquilinoService.buscarInquilino(idInquilino);
                } catch (ErrorServiceException ex) {
                    throw new ErrorServiceException("No se encontró el inquilino seleccionado");
                }
            }
            
            Inmueble inmueble = new Inmueble();
            inmueble.setId(UUID.randomUUID().toString());
            inmueble.setPiso(piso);
            inmueble.setPropietario(propietario);
            inmueble.setInquilino(inquilino);
            inmueble.setDpto(dpto);
            inmueble.setEstadoInmueble(estadoInmueble);
            inmueble.setEliminado(false);

            dao.guardarInmueble(inmueble);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema.");
        }
    }

    /**
     * Modifica un Inmueble existente.
     * @param idInmueble ID del inmueble.
     * @param piso Nuevo número de piso.
     * @param dpto Nuevo número de departamento.
     * @param estadoInmueble Nuevo estado del inmueble.
     * @throws ErrorServiceException
     */
    public void modificarInmueble(String idInmueble, String piso, String dpto, EstadoInmueble estadoInmueble, String idPropietario, String idInquilino) throws ErrorServiceException {
        try {
            Inmueble inmueble = buscarInmueble(idInmueble);
            Propietario propietario;
            Inquilino inquilino;

            if (piso == null || piso.isEmpty() || dpto == null || dpto.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el piso y el departamento.");
            }

            // Verificar si el nuevo piso y departamento ya está siendo usado por otro inmueble.
            try {
                Inmueble inmuebleExistente = dao.buscarInmueblePorPisoYDpto(piso, dpto);
                if (!inmuebleExistente.getId().equals(idInmueble)) {
                    throw new ErrorServiceException("Ya existe un inmueble con el mismo piso y departamento.");
                }
            } catch (NoResultDAOException ex) {
                // No existe conflicto, podemos modificar.
            }
            try {
                propietario = propietarioService.buscarPropietario(idPropietario);
            } catch (ErrorServiceException ex) {
                throw new ErrorServiceException("No se encontró el propietario seleccionado");
            }
            
            if (propietario.getHabitaConsorcio()) {
                inquilino = null;
            } else {
                try {
                    inquilino = inquilinoService.buscarInquilino(idInquilino);
                } catch (ErrorServiceException ex) {
                    throw new ErrorServiceException("No se encontró el inquilino seleccionado");
                }
            }
            inmueble.setPropietario(propietario);
            inmueble.setInquilino(inquilino);
            inmueble.setPiso(piso);
            inmueble.setDpto(dpto);
            inmueble.setEstadoInmueble(estadoInmueble);

            dao.actualizarInmueble(inmueble);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema.");
        }
    }

    /**
     * Busca un inmueble por su ID.
     * @param id ID del inmueble.
     * @return Inmueble encontrado.
     * @throws ErrorServiceException
     */
    public Inmueble buscarInmueble(String id) throws ErrorServiceException {
        try {
            if (id == null || id.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el ID del inmueble.");
            }

            Inmueble inmueble = dao.buscarInmueble(id);

            if (inmueble.getEliminado()) {
                throw new ErrorServiceException("El inmueble está eliminado.");
            }

            return inmueble;

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema.");
        }
    }

    /**
     * Elimina (lógicamente) un inmueble.
     * @param id ID del inmueble.
     * @throws ErrorServiceException
     */
    public void eliminarInmueble(String id) throws ErrorServiceException {
        try {
            Inmueble inmueble = buscarInmueble(id);
            inmueble.setEliminado(true);
            dao.actualizarInmueble(inmueble);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema.");
        }
    }

    /**
     * Lista todos los inmuebles activos (no eliminados).
     * @return Colección de inmuebles activos.
     * @throws ErrorServiceException
     */
    public Collection<Inmueble> listarInmueblesActivos() throws ErrorServiceException {
        try {
            return dao.listarInmueblesActivos();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema.");
        }
    }
    
    /**
     * Obtiene el nombre del responsable del inmueble
     * @param inmueble Inmueble
     * @return String
     */
    public String obtenerResponsable(Inmueble inmueble) {
        Propietario propietario = inmueble.getPropietario();
        Inquilino inquilino = inmueble.getInquilino();
        
        if (inquilino != null) {
            return inquilino.getNombreApellidoMail();
        } else {
            return propietario.getNombreApellidoMail();
        } 
    }
    
    /**
     * Obtiene el mail del responsable del inmueble
     * @param inmueble Inmueble
     * @return String
     * @deprecated 
     */
    public String obtenerMailResponsable(Inmueble inmueble) {
        Propietario propietario = inmueble.getPropietario();
        Inquilino inquilino = inmueble.getInquilino();
        
        if (inquilino != null) {
            return inquilino.getCorreoElectronico();
        } else {
            return propietario.getCorreoElectronico();
        } 
    }
}
