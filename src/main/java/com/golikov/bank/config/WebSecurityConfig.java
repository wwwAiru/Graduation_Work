package com.golikov.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

// конфигурация Security
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //автоинжект подключения к б.д.
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/deposits", "/registration").permitAll() //полный доступ
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
    }
    // переопределение метода, dataSource - объект подключения к б.д.,
    // passwordEncoder шифрование пароля
    // usersByUsernameQuery поиск пользователя в б.д. по емэйлу
    // authoritiesByUsernameQuery авторизация по ролям
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("SELECT email, password, active FROM clients WHERE email=?")
                .authoritiesByUsernameQuery("SELECT cl.email, cr.roles " +
                                            "FROM clients cl " +
                                            "INNER JOIN clients_roles cr " +
                                            "ON cl.id=cr.client_id " +
                                            "WHERE cl.email=?");
    }
}
