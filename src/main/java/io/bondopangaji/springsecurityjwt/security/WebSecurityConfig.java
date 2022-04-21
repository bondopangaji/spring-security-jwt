/*
 * Copyright (c) 2022 Bondo Pangaji
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */

package io.bondopangaji.springsecurityjwt.security;

import io.bondopangaji.springsecurityjwt.error.security.CustomAccessDeniedHandler;
import io.bondopangaji.springsecurityjwt.error.security.CustomAuthenticationEntryPoint;
import io.bondopangaji.springsecurityjwt.filter.CustomAuthenticationFilter;
import io.bondopangaji.springsecurityjwt.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Web security configuration.
 * @author Bondo Pangaji
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("io.bondopangaji.springsecurityjwt.security")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Dependency injection using lombok @RequiredArgsConstructor.
     * @see RequiredArgsConstructor
     */
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String apiPath = "/api/v1";

        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManagerBean());

        customAuthenticationFilter.setFilterProcessesUrl(apiPath + "/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
        http.authorizeRequests().and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        http.authorizeRequests()
                .antMatchers(
                        apiPath + "/login/**",
                        apiPath + "/token/refresh").permitAll();
        http.authorizeRequests()
                .antMatchers(
                        apiPath + "/user/**",
                        apiPath + "/role/**").hasAnyAuthority("USER", "ADMIN");
        http.authorizeRequests()
                .antMatchers(
                        apiPath + "/user/save/**",
                        apiPath + "/role/save/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public static AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public static AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
