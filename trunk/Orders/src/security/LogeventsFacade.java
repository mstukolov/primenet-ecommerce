package security;


import com.orders.facade.AbstractFacade;
import org.orders.entity.Logevents;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

//Issue 32:	Система логирования действий пользователей
@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class LogeventsFacade extends AbstractFacade<Logevents> implements LogeventsImpl{
    private static Logger _log = Logger.getLogger(LogeventsFacade.class.getName());

    @PersistenceContext(unitName = "OrdersPU")
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public LogeventsFacade() {
        super(Logevents.class);
    }
    @Override
    public void logUserEvents(User user, String action, Date date, String ipAddress){
        // JDBC driver name and database URL

        //final String DB_URL = "jdbc:mysql://mysql-env-4411800.jelasticloud.com:3306/orders";
        final String DB_URL = "jdbc:mysql://localhost:3306/orders";
        // Database credentials
        final String USER = "root";
        final String PASS = "java";
        _log.info("Запись в базу данных о действиях пользователя= " + DB_URL);

        Connection conn = null;
        Statement stmt  = null;
        ResultSet rs = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            System.out.println("Inserting records into the table...");
            stmt = conn.createStatement();

            String values = " VALUES ('" +
                    user.getUsername() + "','" +
                    new SimpleDateFormat("yyyy-MM-dd").format(date) + "','" +
                    ipAddress + "','" +
                    user.getAuthorities().toString() + "','" +
                    "System" + "','" +
                    "System" + "','" +
                     new SimpleDateFormat("yyyy-MM-dd").format(date) + "','" +
                    "1" + "','" +
                    action + "')";

            String sql = "INSERT INTO Logevents (`user`,\n" +
                    "`actiondate`,\n" +
                    "`ipaddress`,\n" +
                    "`role`,\n" +
                    "`created_by`,\n" +
                    "`updated_by`,\n" +
                    "`updated_at`,\n" +
                    "`version`,\n" +
                    "`action`)"

                    + values;

            //_log.info("Код запроса: " + sql);
            stmt.executeUpdate(sql);
            System.out.println("Inserted records into the table...");

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

    }
    //@Override
    @Transactional
    public void logUserEventsJPA(User user, String action, Date date, String ipAddress){

        Logevents event = new Logevents();
        event.setAction(action);
        event.setUser(user.getUsername());
        event.setActiondate(date);
        event.setIpaddress(ipAddress);


        em.persist(event);

        _log.info("Запись в базу данных о действиях пользователя= "
                + user.getUsername()
                + " :Дейстиве: " + action
                + " :Дата:"  + date
                + " :ipAddress: " + ipAddress
        );
    }

    @Override
    public void save(Logevents event) {
        em.merge(event);
    }
}
