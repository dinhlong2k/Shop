package com.example.udemywebbackend.admin.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder());
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/usermanager/**").hasAnyAuthority("Admin")
                .antMatchers("/login").permitAll()
                .antMatchers("/").hasAnyAuthority("Admin","Assistant","Editor","Shipper","Sale")
                .and().formLogin()
                    .loginPage("/login") //trang customLoginPage
                    .usernameParameter("email") //nhan gia tri username la email ben form
                    .defaultSuccessUrl("/",true) //khi thanh cong se truyen den trang
                .and().logout().logoutUrl("/logout")
                .and().rememberMe().key("uniqueAndSecret").tokenValiditySeconds(7 * 24 * 60 *60).userDetailsService(userDetailsService)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).maximumSessions(2).expiredUrl("/login?invalid-session=true");;
    }
}
