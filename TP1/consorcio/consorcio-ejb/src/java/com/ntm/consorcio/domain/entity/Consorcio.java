/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.domain.entity;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Martinotebook
 */
@Entity
public class Consorcio implements Serializable{
    
    @Id
    private String id;
    private String nombre;
    @ManyToOne
    private Direccion direccion;
    private boolean eliminado;
    
    public Consorcio(){
    this.eliminado = false;
}
    
    public Consorcio(String id, String nombre, Direccion direccion){
        this.id=id;
        this.nombre=nombre;
        this.eliminado=Boolean.FALSE;
        this.direccion=direccion;
        
    }
    @Override
    public String toString() {
        return "com.ospelsym.dominio.especialidad.Especialidad[ id=" + id + " ]";
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Consorcio other = (Consorcio) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.nombre, other.nombre);
    }
    
    
    public String getId(){
        return this.id;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public boolean getEliminado() {
        return this.eliminado;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
  
        public Direccion getDireccion() {
       return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}
