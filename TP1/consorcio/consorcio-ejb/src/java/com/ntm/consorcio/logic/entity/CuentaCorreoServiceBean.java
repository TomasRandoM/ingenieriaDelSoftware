package com.ntm.consorcio.logic.entity;

import com.ntm.consorcio.domain.entity.Consorcio;
import com.ntm.consorcio.domain.entity.CuentaCorreo;
import com.ntm.consorcio.logic.ErrorServiceException;
import com.ntm.consorcio.persistence.NoResultDAOException;
import com.ntm.consorcio.persistence.entity.DAOCuentaCorreoBean;
import java.util.Properties;
import java.util.UUID;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Clase que implementa los métodos de CuentaCorreo
 * @version 1.0.0
 * @author Tomas Rando
 */
@Stateless
@LocalBean
public class CuentaCorreoServiceBean {

   private @EJB DAOCuentaCorreoBean dao;
   private @EJB ConsorcioServiceBean consorcioService;
    
   /**
    * Crea una cuenta de correo y llama al dao para persistirla en la base de datos
    * @param correo String
    * @param clave String
    * @param puerto String
    * @param smtp String
    * @param tls boolean
    * @param idConsorcio String
    * @throws ErrorServiceException 
    */
    public void crearCuentaCorreo(String correo, String clave, String puerto, String smtp, boolean tls, String idConsorcio) throws ErrorServiceException {
                
        try {
            Consorcio consorcio = consorcioService.buscarConsorcio(idConsorcio);
            if (!verificar(correo)) {
                throw new ErrorServiceException("Debe indicar el correo");
            }
            
            if (!verificar(clave)) {
                throw new ErrorServiceException("Debe indicar la clave");
            }
            
            if (!verificar(puerto)) {
                throw new ErrorServiceException("Debe indicar el puerto");
            }
            
            if (!verificar(smtp)) {
                throw new ErrorServiceException("Debe indicar el smtp");
            }
            
            if (!verificar(idConsorcio)) {
                throw new ErrorServiceException("Debe indicar el consorcio");
            }
            
            try {
                dao.buscarCuentaCorreoPorCorreo(correo);
                throw new ErrorServiceException("Existe esa cuenta de correo");
            } catch (NoResultDAOException e) {
            }
            
            try {
                dao.buscarCuentaCorreoActiva();
                throw new ErrorServiceException("Existe una cuenta de correo");
            } catch (NoResultDAOException e) {
            }
            
            CuentaCorreo cuentaCorreo = new CuentaCorreo();
            cuentaCorreo.setCorreo(correo);
            cuentaCorreo.setClave(clave);
            cuentaCorreo.setSmtp(smtp);
            cuentaCorreo.setEliminado(false);
            cuentaCorreo.setTls(tls);
            cuentaCorreo.setPuerto(puerto);
            cuentaCorreo.setId(UUID.randomUUID().toString());
            cuentaCorreo.setConsorcio(consorcio);
            
            dao.guardarCuentaCorreo(cuentaCorreo);
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }
    
    /**
     * Modifica una cuenta de correo
     * @param idCuentaCorreo String
     * @param correo String
     * @param clave String
     * @param puerto String
     * @param smtp String
     * @param tls boolean
     * @param idConsorcio String
     * @throws ErrorServiceException 
     */
    public void modificarCuentaCorreo(String idCuentaCorreo, String correo, String clave, String puerto, String smtp, boolean tls, String idConsorcio) throws ErrorServiceException {

        try {

            CuentaCorreo cuentaCorreo = buscarCuentaCorreo(idCuentaCorreo);
            Consorcio consorcio = consorcioService.buscarConsorcio(idConsorcio);
            if (!verificar(correo)) {
                throw new ErrorServiceException("Debe indicar el correo");
            }
            
            if (!verificar(clave)) {
                throw new ErrorServiceException("Debe indicar la clave");
            }
            
            if (!verificar(puerto)) {
                throw new ErrorServiceException("Debe indicar el puerto");
            }
            
            if (!verificar(smtp)) {
                throw new ErrorServiceException("Debe indicar el smtp");
            }
            
            if (!verificar(idConsorcio)) {
                throw new ErrorServiceException("Debe indicar el consorcio");
            }

            try {
                CuentaCorreo cuentaCorreoExistente = dao.buscarCuentaCorreoPorCorreo(correo);
                if (!cuentaCorreoExistente.getId().equals(idCuentaCorreo)){
                  throw new ErrorServiceException("Existe una cuenta de correo con el mismo correo");  
                }
            } catch (NoResultDAOException ex) {
            }

            cuentaCorreo.setCorreo(correo);
            cuentaCorreo.setClave(clave);
            cuentaCorreo.setSmtp(smtp);
            cuentaCorreo.setTls(tls);
            cuentaCorreo.setPuerto(puerto);
            cuentaCorreo.setConsorcio(consorcio);
            
            dao.actualizarCuentaCorreo(cuentaCorreo);

        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }
    }

    /**
     * Busca el objeto y lo devuelve si lo encuentra
     * @param id String con el id
     * @return Objeto CuentaCorreo
     * @throws ErrorServiceException 
     */
    public CuentaCorreo buscarCuentaCorreo(String id) throws ErrorServiceException {

        try {
            
            if (!verificar(id)) {
                throw new ErrorServiceException("Debe indicar la cuenta de correo");
            }

            CuentaCorreo cuentaCorreo = dao.buscarCuentaCorreo(id);
            
            if (cuentaCorreo.isEliminado()){
                throw new ErrorServiceException("No se encuentra el correo indicado");
            }

            return cuentaCorreo;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    /**
     * Elimina el objeto
     * @param id String que representa el id
     * @throws ErrorServiceException 
     */
    public void eliminarCuentaCorreo(String id) throws ErrorServiceException {

        try {

            CuentaCorreo cuentaCorreo = buscarCuentaCorreo(id);
            cuentaCorreo.setEliminado(true);
            
            dao.actualizarCuentaCorreo(cuentaCorreo);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }

    /**
     * Devuelve una CuentaCorreo con los objetos de la clase activos
     * @return CuentaCorreo
     * @throws ErrorServiceException 
     */
    public CuentaCorreo listarCuentaCorreoActiva() throws ErrorServiceException {
        try {
            return dao.buscarCuentaCorreoActiva();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
    /**
     * Verifica que la String no sea vacía o nula.
     * @param st String
     * @return boolean
     */
    public boolean verificar(String st) {
        if (st.isEmpty() || st == null) {
            return false;
        }
        return true;
    }
    
    /**
     * Envia el email con un archivo adjunto
     * @param destino String
     * @param asunto String
     * @param cuerpo String
     * @param path String
     * @throws ErrorServiceException 
     */
    public void sendEmail(String destino, String asunto, String cuerpo, String path) throws ErrorServiceException {
        //Comento todo ya que es una funcionalidad nueva
        try {
            //recupera la cuenta de correo del consorcio
            CuentaCorreo cuentaCorreo = listarCuentaCorreoActiva();
            //Recuperamos la direccion origen
            final String direccionDesde = cuentaCorreo.getCorreo();
            //Recuperamos la clave
            final String contrasenia = cuentaCorreo.getClave();
            
            //Se crea un properties que almacena la configuracion (almacenada en cuentaCorreo)
            Properties propiedades = new Properties();
            propiedades.put("mail.smtp.host", cuentaCorreo.getSmtp());
            propiedades.put("mail.smtp.port", cuentaCorreo.getPuerto());
            propiedades.put("mail.smtp.auth", "true");
            propiedades.put("mail.smtp.starttls.enable", cuentaCorreo.isTls());
            
            //Crea una sesión del correo con autenticación
            Session session = Session.getInstance(propiedades, new Authenticator() {
                //Obtener credenciales de autenticación
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(direccionDesde, contrasenia);
                }
            });

            // Creamos el mensaje
            Message mensaje = new MimeMessage(session);
            // Seteamos la dirección de origen
            mensaje.setFrom(new InternetAddress(direccionDesde));
            // Seteamos la dirección de destino
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
            // Seteamos el asunto
            mensaje.setSubject(asunto);

            // Creamos el cuerpo del mensaje o correo
            MimeBodyPart cuerpoMensaje = new MimeBodyPart();
            // Seteamos el cuerpo en el MimeBodyPart
            cuerpoMensaje.setText(cuerpo);

            // Creamos nueva parte del mensaje para el archivo que generamos
            MimeBodyPart attachmentPart = new MimeBodyPart();
            // Buscamos el archivo que necesitamos adjuntar
            DataSource source = new FileDataSource(path);
            // Asociamos el archivo al MimeBodyPart
            attachmentPart.setDataHandler(new DataHandler(source));
            //Nombre del archivo en el correo
            attachmentPart.setFileName(path);

            // Se combinan las partes en una
            Multipart multipart = new MimeMultipart();
            //Agregamos el cuerpo del mensaje
            multipart.addBodyPart(cuerpoMensaje);
            // Agregamos la parte que lleva el archivo
            multipart.addBodyPart(attachmentPart);
            
            //Agregamos el multipart al mensaje
            mensaje.setContent(multipart);
            
            // Enviamos el correo
            Transport.send(mensaje);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new ErrorServiceException("Error generando el mail");
        } catch (ErrorServiceException e) {
            throw e;
        }
    }
}
