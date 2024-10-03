package com.ntm.consorcio.domain.entity;

import com.ntm.consorcio.domain.entity.Provincia;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
/**
 * Representa la información esencial y las características de un departamento.
 * @version 1.0.0
 * @author Tomas Rando
 */

@Entity
public class Departamento implements Serializable {
    @Id
    private String id;
    private String nombre;
    private boolean eliminado;
    @ManyToOne
    private Provincia provincia;
   
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
        if (!(object instanceof Departamento)) {
            return false;
        }
        Departamento other = (Departamento) object;
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
        return "com.ntm.consorcio.dominio.entity.Departamento[ id=" + id + " ]";
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
     * Getter de provincia
     * @return Provincia
     */
    public Provincia getProvincia() {
        return this.provincia;
    }
    
    /**
     * Setter de id
     * @param id String
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Setter de nombre del objeto
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
    
    /**
     * Setter de provincia
     * @param provincia Provincia
     */
    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
}
