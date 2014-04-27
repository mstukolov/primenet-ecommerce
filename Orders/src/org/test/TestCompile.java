package org.test;


import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.util.Properties;

public class TestCompile {


    public static void main(String[] args) {

        System.out.println("Hello from java!");

        final String username = "maxim.stukolov@gmail.com";
        final String password = "carter2014!";

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
                    InternetAddress.parse("smb81@yandex.ru"));
            message.setSubject("Электронный магазин PrimeShop");
            message.setText("Уважаемый клиент,"
                    + "\n\n Просьба ознакомиться с текущими акциями!"
                    +"\n\n" +
                    getMessage());

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
    public static String getMessage(){
        System.out.println("Start build message.................");
        /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        /*  next, get the Template  */

        Template t = ve.getTemplate("./src/mail/velocity/send_order_status.vm");
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("name", "World");
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        /* show the World */
        return writer.toString();
    }
}
