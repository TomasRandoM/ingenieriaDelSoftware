package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Clase que representa a perfil
 * @version 1.0.0
 * @author Tomas Rando
 */
@Entity
public class Perfil implements Serializable {
    @Id
    private String id;
    private String nombre;
    private String detalle;
    private boolean eliminado;
    @OneToMany
    private Collection<Menu> menu;

    /**
     * Getter de id
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Setter de id
     * @param id String
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Getter de nombre
     * @return String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter de nombre
     * @param nombre String
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter de detalle
     * @return String
     */
    public String getDetalle() {
        return detalle;
    }

    /**
     * Setter de detalle
     * @param detalle String
     */
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    /**
     * Getter de eliminado
     * @return boolean
     */
    public boolean isEliminado() {
        return eliminado;
    }

    /**
     * Setter de eliminado
     * @param eliminado boolean
     */
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    /**
     * Getter de menu
     * @return Collection
     */
    public Collection<Menu> getMenu() {
        return menu;
    }

    /**
     * Setter de menu
     * @param menu Collection
     */
    public void setMenu(Collection<Menu> menu) {
        this.menu = menu;
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
        if (!(object instanceof Perfil)) {
            return false;
        }
        Perfil other = (Perfil) object;
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
        return "com.ntm.consorcio.domain.entity.Perfil[ id=" + id + " ]";
    }
    
}
