package mail.controllers;

import com.orders.facade.UserFacade;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.orders.entity.Customer;
import org.orders.entity.Orders;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Logger;
/*[Issue 28]:	Автоматическая рассылка при изменении статуса заказа*/
@Stateless
public class MailFacade {
    private static Logger _log = Logger.getLogger(MailFacade.class.getName());
    @EJB
    private UserFacade userFacade;
    final String username = "maxim.stukolov@gmail.com";
    final String password = "carter2014!";
    final String subject = "Электронный магазин PrimeShop";

    private Properties p;
    private VelocityEngine ve;

    public void init(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String path = servletContext.getRealPath("/");

        p = new Properties();

        p.setProperty( "resource.loader", "class" );
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        p.setProperty( Velocity.FILE_RESOURCE_LOADER_PATH, path );
        p.setProperty("input.encoding", "UTF-8");
        p.setProperty("output.encoding", "UTF-8");
        /*  first, get and initialize an engine  */
        ve = new VelocityEngine();
        ve.init(p);
    }
    public void send(String body, String customer){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from-email@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(customer));
            message.setSubject("Электронный магазин PrimeShop");
            message.setText(body);

            Transport.send(message);

            _log.info("Письмо отправлено");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendOrderStatusToCustomer(Orders order){
        init();
        //Template t = ve.getTemplate(templatesPath + "send_order_status.vm");
        Template t = ve.getTemplate("\\mail\\velocity\\send_order_status.vm");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("name", order.getCustomer());
        context.put("order", order.getSalesId());
        context.put("status", order.getStatus());

        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        /* show the World */
        send(writer.toString(), order.getCustomer());
    }
    public void sendRegistrationInfo(Customer customer){
        init();
        //Template t = ve.getTemplate(templatesPath + "send_order_status.vm");
        Template t = ve.getTemplate("\\mail\\velocity\\send_register_info.vm");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("name", customer.getName());
        context.put("login", customer.getUser());
        context.put("password", userFacade.findUserByLogin(customer.getUser()).getPassword());

        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        /* show the World */
        send(writer.toString(), customer.getUser());
    }
}
