package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Clase que representa a persona con los atributos básicos
 * @version 1.0.0
 * @author Tomas Rando
 */
@Entity
public class Persona implements Serializable {
    @Id
    protected String id;
    protected String nombre;
    protected String apellido;
    protected String telefono;
    protected String correoElectronico;
    protected boolean eliminado;
    
    /**
     * Getter de Id
     * @return String id
     */
    public String getId() {
        return id;
    }
    
    public String getNombreApellido() {
        return this.nombre + " " + this.apellido;
    }
    
    /**
     * Getter de Nombre
     * @return String Nombre
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Getter de Apellido
     * @return String Apellido
     */
    public String getApellido() {
        return apellido;
    }
    
    /**
     * Getter de Telefono
     * @return String Telefono
     */
    public String getTelefono() {
        return telefono;
    }
    
    /**
     * Getter de CorreoElectronico
     * @return String CorreoElectronico
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    
    /**
     * Getter de eliminado
     * @return boolean eliminado
     */
    public boolean isEliminado() {
        return eliminado;
    }
    
    /**
     * Setter de Id
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
     * Setter de apellido
     * @param apellido String
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    /**
     * Setter de telefono
     * @param telefono String
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    /**
     * Setter de correoElectronico
     * @param correoElectronico String
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    
    /**
     * Setter de eliminado
     * @param eliminado boolean
     */
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
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
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
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
        return "com.ntm.consorcio.domain.entity.Persona[ id=" + id + " ]";
    }
    
    public String getNombreApellidoMail() {
        return this.correoElectronico + "," + this.nombre + " " + this.apellido;
    }
    
}
