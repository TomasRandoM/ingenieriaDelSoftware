package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
/**
 * Representa la información esencial y las características de una provincia
 * @version 1.0.0
 * @author Tomas Rando
 */

@Entity
public class Direccion implements Serializable {
    @Id
    private String id;
    private String calle;
    private String numeracion;
    private boolean eliminado;
    @ManyToOne
    private Localidad localidad;
   
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
        if (!(object instanceof Direccion)) {
            return false;
        }
        Direccion other = (Direccion) object;
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
        return "com.ntm.consorcio.dominio.entity.Direccion[ id=" + id + " ]";
    }
    
    /**
     * Getter del id
     * @return String id
     */
    public String getId(){
        return this.id;
    }
    
    /**
     * Getter de numeración
     * @return String numeración
     */
    public String getNumeracion() {
        return this.numeracion;
    }
    
    /**
     * Getter del valor de eliminado
     * @return boolean
     */
    public boolean getEliminado() {
        return this.eliminado;
    }
    
    /**
     * Getter de la calle
     * @return String calle
     */
    public String getCalle() {
        return this.calle;
    }
    
    /**
     * Getter de la localidad
     * @return Localidad localidad;
     */
    public Localidad getLocalidad() {
        return this.localidad;
    }
    
    /**
     * Setter de id
     * @param id String
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Setter de la numeración del departamento
     * @param numeracion String 
     */
    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }
    
    /**
     * Setter del valor eliminado
     * @param eliminado boolean
     */
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    
    /**
     * Setter de la calle
     * @param calle String con la calle
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }
    
    /**
     * Setter de localidad
     * @param localidad Localidad
     */
    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }
    
    /**
     * Getter de calle + numeración
     * @return String
     */
    public String getCalleNumeracion() {
        return this.calle + " " + this.numeracion;
    }
}
