package com.simplebank.security.filter;

import static com.simplebank.security.SecurityConstants.ACCESS_TOKEN;
import static com.simplebank.security.SecurityConstants.BEARER;
import static com.simplebank.security.SecurityConstants.ERROR;
import static com.simplebank.security.SecurityConstants.ERROR_MSG;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplebank.security.SecurityConstants;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.filter.OncePerRequestFilter;

public class WebAppAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!request.getServletPath().equals("/login")) {
            checkAuthorization(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private void checkAuthorization(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authenticationToken = decodeAuthToken(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            response.setHeader(ERROR, e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put(ERROR_MSG, e.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);

            ObjectMapper om = new ObjectMapper();
            om.writeValue(response.getOutputStream(), error);
        }
    }

    private UsernamePasswordAuthenticationToken decodeAuthToken(String authorizationHeader) {
        Algorithm algorithm = Algorithm.HMAC256(SecurityConstants.SECRET.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        String authToken = authorizationHeader.substring(BEARER.length());
        DecodedJWT decodedJWT = verifier.verify(authToken);

        List<String> roles = decodedJWT.getClaim(SecurityConstants.ROLES).asList(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));  
        });
        String username = decodedJWT.getSubject();

        UsernamePasswordAuthenticationToken authorizationToken = 
            new UsernamePasswordAuthenticationToken(username, null, authorities);
        
        return authorizationToken;
    }
}