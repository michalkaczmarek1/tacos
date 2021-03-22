package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserRepositoryUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests()
//                    .antMatchers("/design", "/orders")
//                        .hasRole("ROLE_USER")
//                    .antMatchers("/", "/**")
//                        .permitAll();
        httpSecurity.authorizeRequests()
                    .antMatchers("/design", "/orders/current")
                        .access("hasRole('ROLE_USER')")
                    .antMatchers("/", "/**")
                        .access("permitAll")
                    .and()
                        .formLogin()
                            .loginPage("/login")
                    .and()
                        .logout()
                            .logoutSuccessUrl("/")
                    .and()
                        .csrf()
                            .ignoringAntMatchers("/h2-console/**")
                    .and()
                        .headers()
                            .frameOptions()
                                .sameOrigin();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new StandardPasswordEncoder("53cr3t");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(encoder());
    }

//    @Autowired
//    DataSource dataSource;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("buzz")
//                    .password("infinity")
//                    .authorities("ROLE_USER")
//                .and()
//                .withUser("woody")
//                    .password("bullseye")
//                    .authorities("ROLE_USER");
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//       auth.jdbcAuthentication()
//               .dataSource(dataSource)
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select username, password, enabled from Users where username = ?")
//                .authoritiesByUsernameQuery("select username, authority from UserAuthorities where username = ?")
//                .passwordEncoder(new StandardPasswordEncoder("53cr3t"));
//
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.ldapAuthentication()
//                .userSearchBase("ou=people")
//                .userSearchFilter("(uid={0})")
//                .groupSearchBase("ou=groups")
//                .groupSearchFilter("member={0}")
//                .contextSource()
//                .root("dc=tacos,dc=com")
//                .ldif("classpath:users.ldif")
//                .and()
//                .passwordCompare()
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .passwordAttribute("passcode");
//    }
}
