package security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static Logger logger = Logger.getLogger(MySimpleUrlAuthenticationSuccessHandler.class.getName());
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @EJB(name="logeventsFacadeImpl")
    private LogeventsImpl logeventsFacade;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request,
                          HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication, request);

        if (response.isCommitted()) {
            logger.info("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    /** Builds the target URL according to the logic defined in the main class Javadoc. */
    protected String determineTargetUrl(Authentication authentication, HttpServletRequest request) {
        boolean isUser = false;
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        //Issue 32:	Система логирования действий пользователей
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String ipAddress = request.getRemoteAddr();

        if (isUser) {
            //Issue 32:	Система логирования действий пользователей
            logeventsFacade.logUserEvents((User)principal, "Logged IN", new Date(), ipAddress);
            return "/index.xhtml";
        } else if (isAdmin) {
            //Issue 32:	Система логирования действий пользователей
            logeventsFacade.logUserEvents((User)principal, "Logged IN", new Date(), ipAddress);
            return "/dashboard.xhtml";
        } else {
            throw new IllegalStateException();
        }
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}
