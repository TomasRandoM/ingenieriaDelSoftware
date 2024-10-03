package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Clase que representa la nacionalidad
 * @version 1.0.0
 * @author Tomas Rando
 */
@Entity
public class Nacionalidad implements Serializable {
    @Id
    private String id;
    private String nombre;
    private boolean eliminado;

    /**
     * Getter de Id
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Getter de nombre
     * @return String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Getter de eliminado
     * @return boolean
     */
    public boolean isEliminado() {
        return eliminado;
    }

    /**
     * Setter de id
     * @param id String
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter de nombre
     * @param nombre String
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Setter de eliminado
     * @param eliminado boolean
     */
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    
    /**
     * Crea un código hash para un objeto. Si id no es null se llama a su método hashCode, caso contrario se utiliza 0.
     * @return Int que representa el hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compara dos objetos para determinar si son equivalentes
     * @param object Objeto con el que estamos comparando la instancia actual
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nacionalidad)) {
            return false;
        }
        Nacionalidad other = (Nacionalidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    /**
     * Devuelve representación en String del objeto
     * @return String
     */
    @Override
    public String toString() {
        return "com.ntm.consorcio.domain.entity.Nacionalidad[ id=" + id + " ]";
    }
    
}
