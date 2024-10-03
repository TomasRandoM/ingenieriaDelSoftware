package com.consorcio.controller;

import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Controlador de autenticación que implementa PhaseListener para verificar si el usuario está
 * autenticado en la sesión cuando se accede a vistas protegidas.
 * @version 1.0.0
 * @author Tomas Rando
 */
public class ControllerAutenticacion implements PhaseListener {
    
    /**
     * Método que se ejecuta después de la fase de restauración
     * @param event PhaseEvent
     */
    @Override
    public void afterPhase(PhaseEvent event) {
        
        FacesContext context = event.getFacesContext();
        
        //Si existe el usuario en la sesión, el sistema no hace nada
        if (existeUsuarioEnSession(context)) {
            return;
        } else {
            //Si no existe el usuario en la sesión, se verifica si se está accediendo a una vista protegida.
            if (requestingSecureView(context)) {
                //Se redirige al usuario a la página de logout
                NavigationHandler n = context.getApplication().getNavigationHandler();
                n.handleNavigation(context, null, "logout");

            }
        }
    }

    /**
     * Verifica si la vista está dentro de las rutas protegidas
     * @param context FacesContext
     * @return boolean
     */
    private boolean requestingSecureView(FacesContext context) {
        boolean loginPage = true;
        //Si la vista no está en la ruta /admin/, no es una ruta protegida.
        if ((context.getViewRoot().getViewId().lastIndexOf("/admin/") == -1)) {
            loginPage = false;
        }
        return loginPage;
    }
    
    /**
     * Método antes de la fase
     * @param event PhaseEvent
     */
    @Override
    public void beforePhase(PhaseEvent event) {}

    /**
     * El PhaseListener se ejecutará en la fase de RESTORE_VIEW
     * @return PhaseId
     */
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    /**
     * Verifica si el usuario está en la sesión buscando "usuarioLogin" en la sesión
     * @param context FacesContext
     * @return boolean
     */
    private boolean existeUsuarioEnSession(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        return (extContext.getSessionMap().containsKey("usuarioLogin"));
    }


}