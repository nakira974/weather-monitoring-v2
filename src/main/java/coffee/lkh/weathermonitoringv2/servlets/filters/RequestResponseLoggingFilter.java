package coffee.lkh.weathermonitoringv2.servlets.filters;

import coffee.lkh.weathermonitoringv2.models.logging.CachedHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@WebFilter(filterName = "RequestResponseLoggingFilter", urlPatterns = "/*")
public class RequestResponseLoggingFilter extends GenericFilterBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (!(servletRequest instanceof HttpServletRequest)) {
            // handle non-HTTP requests here
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest((HttpServletRequest) servletRequest);
        LOGGER.info("REQUEST DATA: " + IOUtils.toString(cachedHttpServletRequest.getInputStream(), String.valueOf(StandardCharsets.UTF_8)));

        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(cachedHttpServletRequest, wrappedResponse);

        byte[] responseData = wrappedResponse.getContentAsByteArray();
        if (responseData.length > 0) {
            LOGGER.info("RESPONSE DATA: {}", new String(responseData));
            wrappedResponse.copyBodyToResponse();
        }
    }
}
