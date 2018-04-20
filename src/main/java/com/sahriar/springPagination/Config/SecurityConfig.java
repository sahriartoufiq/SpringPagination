package com.sahriar.springPagination.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by toufiq on 4/20/18.
 */

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
  //  @Qualifier("ojsuserDetailsService")
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity https) throws Exception {

        https.authorizeRequests()
                .requestMatchers(EndpointRequest.to("userList")).hasRole("ADMIN")
               // .requestMatchers(EndpointRequest.toAnyEndpoint()).authenticated()
               // .antMatchers("/**").authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/login").failureUrl("/login?error")
                .usernameParameter("name")
                .passwordParameter("password")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout")

                .and().exceptionHandling().accessDeniedPage("/403")

                .and().csrf().disable()

			/*.and()
            .sessionManagement()
		    .maximumSessions(1) // How many session the same user can have? This can be any number you pick
		    .expiredUrl("/login?logout")
		    .sessionRegistry(sessionRegistry())*/;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

}
