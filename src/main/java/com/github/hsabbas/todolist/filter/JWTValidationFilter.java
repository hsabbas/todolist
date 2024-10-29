package com.github.hsabbas.todolist.filter;

import com.github.hsabbas.todolist.constants.APIPaths;
import com.github.hsabbas.todolist.constants.EnvironmentVariables;
import com.github.hsabbas.todolist.constants.JWTConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JWTValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(JWTConstants.JWT_HEADER);
        Environment environment = getEnvironment();
        Assert.notNull(environment, "Environment can't be null");
        String secretKeyString = environment.getRequiredProperty(EnvironmentVariables.JWT_SECRET);
        Assert.notNull(environment, "Secret Key environment variable is required");
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
        try {
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
        return request.getServletPath().equals(APIPaths.LOGIN);
    }
}
