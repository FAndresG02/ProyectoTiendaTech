package com.ec.tecnologia.security;

import com.ec.tecnologia.config.CorsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Le indica a Spring que esta clase contiene configuración de la aplicación.
@Configuration
//Activa el módulo de seguridad web de Spring Security.
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CorsConfig corsConfig;

    //Inyección de dependencia para CustomerUsersDetailsService, 
    //que se utiliza para cargar los detalles del usuario durante la autenticación.
    private final CustomerUsersDetailsService customerUsersDetailsService;

    //Constructor para inyectar la dependencia de CustomerUsersDetailsService.
    public SecurityConfig(CustomerUsersDetailsService customerUsersDetailsService) {
        this.customerUsersDetailsService = customerUsersDetailsService;
    }

   //Define un bean de PasswordEncoder que utiliza BCrypt para cifrar las contraseñas de los usuarios.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Configura la cadena de filtros de seguridad para la aplicación.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/users/login",
                                "/users/signup",
                                "/users/forgotPassword",

                                "/uploads/**",

                                // ─── Rutas públicas del ecommerce ───
                                "/product/getProducts",
                                "/product/getProductByCategory/**",
                                "/product/getProductById/**",
                                "/product/getProductByName/**",

                                "/category/getCategories"


                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //Define un bean de JwtAuthenticationFilter que se utiliza para interceptar las solicitudes y validar el token JWT.
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(new JwtUtil(), customerUsersDetailsService);
    }

    //Define un bean de AuthenticationManager que se utiliza para gestionar la autenticación de los usuarios,
    //permite autenticar usuarios con email + password.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
