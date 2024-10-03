package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * Representa la información esencial y las características de un país
 * @version 1.0.0
 * @author Tomas Rando
 */

@Entity
public class Pais implements Serializable {
    @Id
    private String id;
    private String nombre;
    private boolean eliminado;
   
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
        if (!(object instanceof Pais)) {
            return false;
        }
        Pais other = (Pais) object;
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
        return "com.ntm.consorcio.dominio.entity.Pais[ id=" + id + " ]";
    }
    
    /**
     * Getter del id
     * @return String id
     */
    public String getId(){
        return this.id;
    }
    
    /**
     * Getter del nombre
     * @return String nombre
     */
    public String getNombre() {
        return this.nombre;
    }
    
    /**
     * Getter del valor de eliminado
     * @return boolean
     */
    public boolean getEliminado() {
        return this.eliminado;
    }
    
    /**
     * Setter de id
     * @param id String
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Setter de nombre de país
     * @param nombre String 
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Setter del valor eliminado
     * @param eliminado boolean
     */
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    
}
