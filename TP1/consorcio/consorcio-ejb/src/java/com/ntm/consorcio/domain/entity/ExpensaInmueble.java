/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Mauro Sorbello
 */
@Entity
public class ExpensaInmueble implements Serializable {
    @Id
    private String id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date periodo;
    private EstadoExpensaInmueble estado;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaVencimiento;
    @ManyToOne
    private Expensa expensa;
    @ManyToOne
    private Inmueble inmueble;
    private boolean eliminado;
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpensaInmueble)) {
            return false;
        }
        ExpensaInmueble other = (ExpensaInmueble) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    public EstadoExpensaInmueble getEstado() {
        return estado;
    }

    public void setEstado(EstadoExpensaInmueble estado) {
        this.estado = estado;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Expensa getExpensa() {
        return expensa;
    }

    public void setExpensa(Expensa expensa) {
        this.expensa = expensa;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    
    public String getData() {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

        // Transformar la fecha a String
        String fechaPeriodo = formato.format(periodo);
        String fechaVenc = (fechaVencimiento != null) ? formato.format(fechaVencimiento) : "SIN INFORMACIÓN";
        return "Periodo: " + fechaPeriodo + ", Fecha de Vencimiento: " + fechaVenc + ", " + inmueble.getPisoDpto();
    }
}
