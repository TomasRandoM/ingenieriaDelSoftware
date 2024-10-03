package com.consorcio.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Clase para manejar los mensajes y la severidad
 * @version 1.0.0
 * @author Tomas Rando
 */
public class Messages {
    
    /**
     * Método que maneja los mensajes junto al tipo de severidad
     * @param texto String
     * @param type TypeMessages
     */
    public static void show(String texto, TypeMessages type){
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;;
        
        switch(type) {
            
        case ERROR:
            severity = FacesMessage.SEVERITY_ERROR;
            break;
        case ALERTA:
            severity = FacesMessage.SEVERITY_WARN;
            break;
        case MENSAJE:
            severity = FacesMessage.SEVERITY_INFO;
            break;
        case ERRORCRITICO:    
            severity = FacesMessage.SEVERITY_FATAL;
            break;
        }
        // Añade un mensaje al contexto JSF, con un nivel de severidad y un texto especificado.
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "", texto));
        
    }
    
    /**
     * Agrega un mensaje referido al error de sistemas
     */
    public static void errorSystem(){
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Error de Sistemas"));
    }
    
}
