package security;


import org.orders.entity.Logevents;
import org.springframework.security.core.userdetails.User;

import javax.ejb.Remote;
import java.util.Date;

@Remote
public interface LogeventsImpl {
    public void save(Logevents event);
    public void logUserEvents(User user, String action, Date date, String ipAddress);
}
