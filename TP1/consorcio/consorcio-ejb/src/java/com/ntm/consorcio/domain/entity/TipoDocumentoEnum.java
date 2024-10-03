/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntm.consorcio.domain.entity;

/**
 * Enumeración para representar los tipos de documentos
 * @version 1.0.0
 * @author Tomas Rando
 */
public enum TipoDocumentoEnum {
    /**
     * Documento único
     */
    DOCUMENTO_UNICO,
    
    /**
     * Libreta de enrolamiento
     */
    LIBRETA_ENROLAMIENTO, 
    
    /**
     * Libreta cívica
     */
    LIBRETA_CIVICA,
    
    /**
     * Pasaporte
     */
    PASAPORTE,
    
    /**
     * No especificado u otros
     */
    NO_ESPECIFICADO;
}
