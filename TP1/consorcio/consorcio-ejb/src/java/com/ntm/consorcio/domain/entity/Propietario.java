package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Clase que representa el propietario
 * @version 1.0.0
 * @author Tomas Rando
 */
@Entity
public class Propietario extends Persona implements Serializable {
    
    private boolean habitaConsorcio;
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Direccion direccion;

    /**
     * Constructor de Propietario
     */
    public Propietario() {
        super();
    }
    
    /**
     * Getter de habitaConsorcio
     * @return boolean habitaConsorcio
     */
    public boolean getHabitaConsorcio() {
        return habitaConsorcio;
    }
    
    /**
     * Getter de direccion
     * @return Direccion direccion
     */
    public Direccion getDireccion() {
        return direccion;
    }
    
    /**
     * Setter de direccion
     * @param direccion Direccion
     */
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    
    /**
     * Setter de habitaConsorcio
     * @param habitaConsorcio boolean;
     */
    public void setHabitaConsorcio(boolean habitaConsorcio) {
        this.habitaConsorcio = habitaConsorcio;
    }
    
    /**
     * Devuelve un hash que representa el objeto
     * @return int hash 
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
        if (!(object instanceof Propietario)) {
            return false;
        }
        Propietario other = (Propietario) object;
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
        return "com.ntm.consorcio.domain.entity.Propietario[ id=" + id + " ]";
    }
    
}
