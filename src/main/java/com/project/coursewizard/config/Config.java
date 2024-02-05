package com.project.coursewizard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class Config {

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler();
    }

    @Bean
    public UserDetailsManager memoryUserDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        userDetailsManager.setUsersByUsernameQuery("SELECT username, pwd, enabled FROM course_wizard.users where username=?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT user_id, role FROM course_wizard.roles where user_id=?");

        return userDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer->
                        configurer
                                .requestMatchers("/student/**").hasRole("STUDENT")
                                .requestMatchers("/instructor/**").hasRole("INSTRUCTOR")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/**").hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN")
                                .anyRequest().authenticated())
                .formLogin(form ->
                        form.loginPage("/loginPage")
                                .successHandler(customAuthenticationSuccessHandler())
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll())
                .logout(logout -> logout.permitAll()
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedPage("/access-denied"));
        return http.build();
    }
}
