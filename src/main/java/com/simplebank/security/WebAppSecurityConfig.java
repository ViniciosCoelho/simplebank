package com.simplebank.security;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.simplebank.customer.CustomerService;
import com.simplebank.security.filter.WebAppAuthenticationFilter;
import com.simplebank.security.filter.WebAppAuthorizationFilter;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomerService customerService;
    private final PasswordEncoder pwdEncoder;

    public WebAppSecurityConfig(CustomerService customerService, PasswordEncoder pwdEncoder) {
        this.customerService = customerService;
        this.pwdEncoder = pwdEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerService).passwordEncoder(pwdEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(POST, "/login").permitAll();
        http.authorizeRequests().antMatchers(POST, "/customer").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new WebAppAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(new WebAppAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}