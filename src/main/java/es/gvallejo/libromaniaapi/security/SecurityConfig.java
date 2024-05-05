package es.gvallejo.libromaniaapi.security;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	// Aqui se definen las rutas públicas y privadas
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.addFilterBefore(new JWTAuthorizationFilter(), BasicAuthenticationFilter.class)
				.authorizeHttpRequests(
						authRequest -> authRequest.requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
				.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedHeaders(Collections.singletonList("*"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.addAllowedOrigin("*localhost:4200");
						config.addAllowedOrigin("*");
						config.setAllowCredentials(true);
						return config;
					}

				})).build();

	}

	@Bean
	public PasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder();
	}

}
