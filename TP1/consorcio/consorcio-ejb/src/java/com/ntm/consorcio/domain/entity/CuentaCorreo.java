package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Clase que representa la cuenta de correo
 * @version 1.0.0
 * @author Tomas Rando
 */
@Entity
public class CuentaCorreo implements Serializable {
    @Id
    private String id;
    private String correo;
    private String clave;
    private String puerto;
    private String smtp;
    private boolean tls;
    private boolean eliminado;
    @OneToOne
    private Consorcio consorcio;

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
     * Getter de correo
     * @return String
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Setter de correo
     * @param correo String
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    /**
     * Getter de clave
     * @return String
     */
    public String getClave() {
        return clave;
    }

    /**
     * Setter de clave
     * @param clave String
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * Getter de puerto
     * @return String
     */
    public String getPuerto() {
        return puerto;
    }
    
    /**
     * Setter de puerto
     * @param puerto String
     */
    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    /**
     * Getter de smtp
     * @return String
     */
    public String getSmtp() {
        return smtp;
    }

    /**
     * Setter de smtp
     * @param smtp String
     */
    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    /**
     * Getter de tls
     * @return boolean
     */
    public boolean isTls() {
        return tls;
    }

    /**
     * Setter de tls
     * @param tls boolean
     */
    public void setTls(boolean tls) {
        this.tls = tls;
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
     * Getter de consorcio
     * @return Consorcio
     */
    public Consorcio getConsorcio() {
        return consorcio;
    }
    
    /**
     * Setter de consorcio
     * @param consorcio Consorcio
     */
    public void setConsorcio(Consorcio consorcio) {
        this.consorcio = consorcio;
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
        if (!(object instanceof CuentaCorreo)) {
            return false;
        }
        CuentaCorreo other = (CuentaCorreo) object;
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
        return "com.ntm.consorcio.domain.entity.CuentaCorreo[ id=" + id + " ]";
    }
    
}
