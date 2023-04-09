package coffee.lkh.weathermonitoringv2.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String login(HttpServletRequest request, @AuthenticationPrincipal OidcUser user) {
        if (user != null) {
            logger.info("User already logged in: {}", user.getEmail());
            return "redirect:/weather?city=Abbeville&country=US&state=LA";
        } else {
            OAuth2AuthenticationToken token =
                    (OAuth2AuthenticationToken) request.getUserPrincipal();
            logger.info("User is logging in: {}", token.getPrincipal().getAttributes().get("email"));
            return "redirect:/login/oauth2/authorization/keycloak";
        }
    }

}