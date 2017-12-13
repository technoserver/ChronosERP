/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @author john
 */
@EnableWebSecurity
@ComponentScan(basePackageClasses = AppUserDetailsService.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AppUserDetailsService userDetailsService;

//    @Bean
//    public void migrate(){
//        new FlyWay().migration();
//    }
    @Bean
    public ChronosSuccessHandler chronosSuccessHandler() {
        return new ChronosSuccessHandler();
    }

//    @Bean
//    public UsuarioSistema usuarioSistema() {
//        return new UsuarioSistema();
//    }
//    @Autowired
//    private UsuarioSistema usuarioSistema;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/javax.faces.resource/**")
                .antMatchers("/dialogos/**")
                .antMatchers("/error/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new JsfLoginUrlAuthenticationEntryPoint();
        jsfLoginEntry.setLoginFormUrl("/login.xhtml");
        jsfLoginEntry.setRedirectStrategy(new JsfRedirectStrategy());

        JsfAccessDeniedHandler jsfDeniedEntry = new JsfAccessDeniedHandler();
        jsfDeniedEntry.setLoginPath("/error/acessoNegado.xhtml");
        jsfDeniedEntry.setContextRelative(true);
        
        http
            .csrf().disable()
            .headers().frameOptions().sameOrigin()
                .and()
            .authorizeRequests()
                .antMatchers("/login.xhtml").permitAll()
                .antMatchers("/index.xhtml").authenticated()
                //.antMatchers("/modulo/**").hasAnyRole("ADMINISTRADORES") apenas se usa hasAnyRole quando tiver ROLE na frente
                // ex ROLE_ADMINISTRADORES, caso esteja esteja apenas ADMINISTRADORES é usado hasAuthority
                .antMatchers("/modulo/cadastros/**").authenticated()
                .antMatchers("/modulo/cadastros/administrativo/**").hasRole("ADMIN")
                .antMatchers("/modulo/comercial/nfe/**").hasAnyRole("NFE")
                .antMatchers("/modulo/comercial/nfce/**").hasAnyRole("NFCE")
                .antMatchers("/modulo/comercial/mdfe/**").hasAnyRole("MDFE")
                .antMatchers("/modulo/comercial/os/**").hasAnyRole("OS")
                .antMatchers("/modulo/comercial/vendas/**").hasAnyRole("VENDA")
                .antMatchers("/modulo/compras/**").hasAnyRole("COMPRAS")
                .antMatchers("/modulo/configuracoes/**").hasRole("ADMIN")
                .antMatchers("/modulo/estoque/**").hasAnyRole("ESTOQUE")
                .antMatchers("/modulo/financeiro/movimento/**").hasAnyRole("FINANCEIRO")
                .antMatchers("/modulo/financeiro/pagamento/**").hasAnyRole("FINANCEIRO")
                .antMatchers("/modulo/financeiro/recebimento/**").hasAnyRole("FINANCEIRO")
                .antMatchers("/modulo/financeiro/diversos/**").hasAnyRole("FINANCEIRO")
                .antMatchers("/modulo/financeiro/relatorios/**").hasAnyRole("FINANCEIRO")
                .antMatchers("/modulo/gerencial/**").hasRole("ADMIN")
                .antMatchers("/modulo/fiscal/tributacao/**").hasAnyRole("TRIBUTACAO")
                .antMatchers("/modulo/fiscal/sped/**").hasAnyRole("SPED")
                .and()
            .formLogin()
                .loginPage("/login.xhtml")
                .failureUrl("/login.xhtml?invalid=true")
                .successHandler(new ChronosSuccessHandler())
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and()
            .exceptionHandling()
                .accessDeniedPage("/error/acessoNegado.xhtml")
                .authenticationEntryPoint(jsfLoginEntry)
                .accessDeniedHandler(jsfDeniedEntry);
    }
}
