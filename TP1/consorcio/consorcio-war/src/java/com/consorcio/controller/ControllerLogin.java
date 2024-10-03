
import com.consorcio.controller.Messages;
import com.consorcio.controller.TypeMessages;
import com.ntm.consorcio.domain.entity.Usuario;
import com.ntm.consorcio.logic.entity.UsuarioServiceBean;
import com.ntm.consorcio.logic.entity.initAppServiceBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Controlador de login
 * @version 1.0.0
 * @author Mauro Sorbello
 */
@ManagedBean
@ViewScoped
public class ControllerLogin {
    private @EJB UsuarioServiceBean usuarioService;
    private @EJB initAppServiceBean initService;
    private Usuario usuario;
    
    private String cuenta;
    private String clave;
    
    @PostConstruct
    public void init() {}
    public String aceptar(){
        try {

            initService.iniciarAplicacion();
            usuario = usuarioService.login(cuenta, clave);
            guardarSession(usuario);
            Messages.show("Login exitoso", TypeMessages.MENSAJE);
        }catch (Exception e) {
            e.printStackTrace();
            Messages.show(e.getMessage(), TypeMessages.ERROR);
            
            return null;
        }
        return "listMenu";           
    }
    
    private void guardarSession(Usuario usuario){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(true);
        session.setAttribute("usuarioLogin", usuario);  
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login";  // Este valor debe coincidir con un caso de navegación
    }

    public UsuarioServiceBean getUsuarioService() {
        return usuarioService;
    }

    public void setUsuarioServiceBean(UsuarioServiceBean usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    
}
