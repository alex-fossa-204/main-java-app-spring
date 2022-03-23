package by.andersen.intensive.yellow.team.main.security.listener;

import by.andersen.intensive.yellow.team.main.service.impl.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener {
    private LoginAttemptService loginAttemptService;

    @Autowired
    public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        Object principle = event.getAuthentication().getPrincipal();
        if (principle instanceof String) {
            String userName = (String) event.getAuthentication().getPrincipal();
            loginAttemptService.addUserToLoginAttemptCache(userName);
        }
    }


}
