package com.sahriar.springPagination.Config;

import com.sahriar.springPagination.service.imp.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

/**
 * Created by toufiq on 4/20/18.
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity https) throws Exception {


        https.authorizeRequests().
                antMatchers("/", "/addUser", "/resources/**", "/upload/**", "/images/**", "/webjars/**", "/css/**").permitAll()
               // .antMatchers("/userList").hasAnyAuthority("admin")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll().failureUrl("/login?error")
                .usernameParameter("userName")
                .passwordParameter("password")
                .and()
                .logout().permitAll().logoutUrl("/logout").logoutSuccessUrl("/login?logout").invalidateHttpSession(true)
        //.and()
        //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).maximumSessions(1).maxSessionsPreventsLogin(true);
        //    .and().sessionFixation().none()
        // .and().exceptionHandling().accessDeniedPage("/403")
        //  .and().csrf().disable()
        ;

//        https
//                .sessionManagement()
//                .maximumSessions(1)
//                .expiredUrl("/login?expired")
//                .maxSessionsPreventsLogin(true)
//                .and()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .invalidSessionUrl("/");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }


}
