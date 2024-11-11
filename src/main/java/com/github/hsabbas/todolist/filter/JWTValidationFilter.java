package com.github.hsabbas.todolist.filter;

import com.github.hsabbas.todolist.constants.APIPaths;
import com.github.hsabbas.todolist.constants.EnvironmentVariables;
import com.github.hsabbas.todolist.constants.JWTConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Component
public class JWTValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Environment environment = getEnvironment();
        Assert.notNull(environment, "Environment can't be null");
        String secretKeyString = environment.getRequiredProperty(EnvironmentVariables.JWT_SECRET);
        Assert.notNull(environment, "Secret Key environment variable is required");
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

        try {
            Cookie[] cookies = request.getCookies();
            if(cookies == null) {
                throw new IllegalArgumentException();
            }
            Cookie tokenCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(JWTConstants.COOKIE_NAME)).findFirst().orElse(null);
            if(tokenCookie == null) {
                throw new IllegalArgumentException();
            }
            String jwtToken = tokenCookie.getValue();
            Claims claims = Jwts.parser().verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();
            String email = claims.getSubject();
            String authorities = String.valueOf(claims.get(JWTConstants.AUTHORITIES_CLAIM));
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        } catch (SignatureException exception) {
            log.error("Incorrect JWT token received.");
        } catch (MalformedJwtException exception) {
            log.error("Malformed JWT token received.");
        } catch (ExpiredJwtException exception){
            log.error("Expired JWT token received.");
        } catch (UnsupportedJwtException exception) {
            log.error("Unsupported JWT token received");
        } catch (IllegalArgumentException exception) {
            log.error("JWT token missing");
        }

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals(APIPaths.LOGIN) || request.getServletPath().equals(APIPaths.LOGOUT);
    }
}
