package coffee.lkh.weathermonitoringv2.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class LoginController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuerUri;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model,
                        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                        OAuth2AuthenticationToken authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            // Redirect to home page if user is already authenticated
            return "redirect:/";
        }

        // Create a new CSRF token
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // Pass the token to the view
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }

        if (authorizedClient == null) {
            // Construct the URL for the Keycloak login page
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(issuerUri)
                    .path("/auth/realms/weathermonitoring/protocol/openid-connect/auth")
                    .queryParam("response_type", "code")
                    .queryParam("client_id", clientId)
                    .queryParam("redirect_uri", getRedirectUri(request))
                    .queryParam("scope", "openid");

            return "redirect:" + builder.buildAndExpand("weathermonitoring").toUriString();
        } else {
            // Save the authorized client details for the authenticated user
            authorizedClientService.saveAuthorizedClient(authorizedClient, authentication);

            // Redirect to the home page for authenticated users
            return "redirect:/";
        }
    }

    private String getRedirectUri(HttpServletRequest request) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromRequest(request);
        builder.replacePath("/login/oauth2/code/custom");
        return builder.toUriString();
    }
}