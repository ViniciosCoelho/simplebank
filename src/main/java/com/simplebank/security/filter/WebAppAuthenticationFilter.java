package com.simplebank.security.filter;

import static com.simplebank.security.SecurityConstants.ACCESS_TOKEN;
import static com.simplebank.security.SecurityConstants.SECRET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplebank.security.SecurityConstants;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class WebAppAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authManager;
	
	public WebAppAuthenticationFilter(AuthenticationManager authManager) {
		this.authManager = authManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		return authManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User appUser = (User) authResult.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
		String accessToken = JWT.create()
			.withSubject(appUser.getUsername())
			.withIssuedAt(new Date())
			.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
			.withIssuer(request.getRequestURI().toString())
			.withClaim(SecurityConstants.ROLES, appUser.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
			.sign(algorithm);

		Map<String, String> token = new HashMap<>();
		token.put(ACCESS_TOKEN, accessToken);
		response.setContentType(APPLICATION_JSON_VALUE);

		ObjectMapper om = new ObjectMapper();
		om.writeValue(response.getOutputStream(), token);
	}
}