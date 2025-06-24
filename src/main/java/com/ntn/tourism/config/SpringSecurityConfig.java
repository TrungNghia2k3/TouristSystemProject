package com.ntn.tourism.config;

import com.ntn.tourism.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] PUBLIC_URLS = {
            "/register",
            "/images/**",
            "/user/css/**", "/user/fonts/**", "/user/images/**", "/user/js/**", "/user/scss/**",
            "/base/css/**", "/base/js/**", "/base/scss/**", "/base/vendor/**",
            "/registration",
            "/", "/about", "/tour/**", "/hotels/**", "/contact"
    };

    @Autowired
    AuthenticationSuccessHandler successHandler;

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(PUBLIC_URLS).permitAll() // Ai cũng có thể truy cập
                .antMatchers("/admin/**").hasRole("ADMIN") // Chỉ ADMIN mới truy cập
                .antMatchers("/hotel-manager/**").hasRole("HOTEL_MANAGER") // Chỉ HOTEL_MANAGER mới truy cập
                .antMatchers("/tour-manager/**").hasRole("TOUR_MANAGER") // Chỉ TOUR_MANAGER mới truy cập
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").successHandler(successHandler).permitAll()
                .and()
                .logout().invalidateHttpSession(true).clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout").permitAll();
        http.csrf().disable(); // Only if needed for APIs or non-browser clients
        http.headers()
                .frameOptions().sameOrigin() // For embedding frames
                .httpStrictTransportSecurity().disable(); // Example if HTTPS is not enforced
    }
}
