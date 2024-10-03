package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Clase que representa el usuario
 * @version 1.0.0
 * @author Tomas Rando
 */
@Entity
public class Usuario extends Persona implements Serializable {
    
    private String usuario;
    private String clave;
    @ManyToOne
    private Perfil perfil;
    
    /**
     * Constructor de Usuario
     */
    public Usuario() {
        super();
    }
    
    /**
     * Getter de usuario
     * @return String usuario
     */
    public String getUsuario() {
        return usuario;
    }
    
    /**
     * Getter de clave
     * @return String clave
     */
    public String getClave() {
        return clave;
    }
    
    /**
     * Getter de perfil
     * @return Perfil perfil
     */
    public Perfil getPerfil() {
        return perfil;
    }
    
    /**
     * Setter de usuario
     * @param usuario String
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    /**
     * Setter de clave
     * @param clave String
     */
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    /**
     * Setter de perfil
     * @param perfil Perfil
     */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
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
        return "com.ntm.consorcio.domain.entity.Usuario[ id=" + id + " ]";
    }
    
}
