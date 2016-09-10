package com.example.thymeleaf;

import com.example.thymeleaf.common.UserDetailsServiceImpl;
import com.example.thymeleaf.model.User;
import com.example.thymeleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@SpringBootApplication
public class ThymeleafRoleApplication extends WebSecurityConfigurerAdapter implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Value("${info.login.url}")
    private String loginUrl;

    protected String[] whiteUrls = new String[]{"/login", "/register", "/doLogin", "/doRegister", "/"};

    /**
     * remember me key for salt
     */
    public static String rememberMeAppKey = "test";

    public static void main(String[] args) {
        SpringApplication.run(ThymeleafRoleApplication.class, args);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers(whiteUrls).permitAll()
                .antMatchers(loginUrl).permitAll()
                .anyRequest().authenticated()
                .and().rememberMe().key(rememberMeAppKey)
                .and()
                .csrf().disable();
    }

    @Override
    public void run(String... strings) throws Exception {
        User user1 = new User();
        user1.setName("admin");
        user1.setPassword("123456");
        user1.setRole("ROLE_ADMIN");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("ceo");
        user2.setPassword("123456");
        user2.setRole("ROLE_CEO");
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("cto");
        user3.setPassword("123456");
        user3.setRole("ROLE_CTO");
        userRepository.save(user3);

        User user4 = new User();
        user4.setName("employee1");
        user4.setPassword("123456");
        user4.setRole("ROLE_EMPLOYEE");
        userRepository.save(user4);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService customUserDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    @ConditionalOnMissingBean(RememberMeServices.class)
    public RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(rememberMeAppKey, customUserDetailsService());
        rememberMeServices.setAlwaysRemember(true);
        //token保存两天
        rememberMeServices.setTokenValiditySeconds(60 * 60 * 8);
        rememberMeServices.setUseSecureCookie(false);
        return rememberMeServices;
    }
}
