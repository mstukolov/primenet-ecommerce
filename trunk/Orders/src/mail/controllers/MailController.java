package mail.controllers;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Logger;

@ManagedBean(name="mailController")
@SessionScoped
public class MailController {
    private static Logger _log = Logger.getLogger(MailController.class.getName());

    @EJB
    private MailFacade mailFacade;

    @PostConstruct
    public void init(){

    }

    public void sendMail(){
       //mailFacade.send();
       addMessage("Письмо отправлено");
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
