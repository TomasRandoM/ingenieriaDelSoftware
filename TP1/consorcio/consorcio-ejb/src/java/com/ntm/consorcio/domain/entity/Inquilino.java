package com.ntm.consorcio.domain.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 * Clase que representa al inquilino
 * @version 1.0.0
 * @author Tomas Rando
 */
@Entity
public class Inquilino extends Persona implements Serializable {
    
    private String documento;
    private TipoDocumentoEnum tipoDocumento;
    private SexoEnum sexo;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaNacimiento;
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Nacionalidad nacionalidad;
    
    /**
     * Constructor de Inquilino
     */
    public Inquilino() {
        super();
    }

    /**
     * Getter de documento
     * @return String documento
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Getter de tipoDocumento
     * @return TipoDocumentoEnum tipoDocumento
     */
    public TipoDocumentoEnum getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Getter de sexo
     * @return SexoEnum sexo
     */
    public SexoEnum getSexo() {
        return sexo;
    }
    
    /**
     * Getter de fechaNacimiento
     * @return Date fechaNacimiento
     */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    /**
     * Setter de documento
     * @param documento String
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    
    /**
     * Setter de tipoDocumento
     * @param tipoDocumento String
     */
    public void setTipoDocumento(TipoDocumentoEnum tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * Setter de sexo
     * @param sexo SexoEnum
     */
    public void setSexo(SexoEnum sexo) {
        this.sexo = sexo;
    }

    /**
     * Setter de fechaNacimiento
     * @param fechaNacimiento Date
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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
        if (!(object instanceof Inquilino)) {
            return false;
        }
        Inquilino other = (Inquilino) object;
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
        return "com.ntm.consorcio.domain.entity.Inquilino[ id=" + id + " ]";
    }

    /**
     * Getter de nacionalidad
     * @return Nacionalidad
     */
    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Setter de nacionalidad
     * @param nacionalidad Nacionalidad
     */
    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
    
    
}
