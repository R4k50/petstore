package petstore.petstore.configs;

import petstore.petstore.entryPoints.UserAuthenticationEntryPoint;
import petstore.petstore.filters.JwtAuthFilter;
import petstore.petstore.providers.UserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig
{
  private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
  private final UserAuthenticationProvider userAuthenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
  {
    http
        .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
        .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider), BasicAuthenticationFilter.class)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers(HttpMethod.POST, "/login", "/register").anonymous()

            .requestMatchers(HttpMethod.GET, "/products").permitAll()
            .requestMatchers(HttpMethod.GET, "/product/{id}").permitAll()
            .requestMatchers(HttpMethod.POST, "/product").permitAll()
            .requestMatchers(HttpMethod.PATCH, "/product/{id}").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/product/{id}").permitAll()

            .requestMatchers(HttpMethod.GET, "/product-categories").permitAll()
            .requestMatchers(HttpMethod.GET, "/product-category/{id}").permitAll()
            .requestMatchers(HttpMethod.POST, "/product-category").permitAll()
            .requestMatchers(HttpMethod.PATCH, "/product-category/{id}").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/product-category/{id}").permitAll()

            .requestMatchers(HttpMethod.GET, "/animals").permitAll()
            .requestMatchers(HttpMethod.GET, "/animal/{id}").permitAll()
            .requestMatchers(HttpMethod.POST, "/animal").permitAll()
            .requestMatchers(HttpMethod.PATCH, "/animal/{id}").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/animal/{id}").permitAll()

            .requestMatchers(HttpMethod.GET, "/animal-categories").permitAll()
            .requestMatchers(HttpMethod.GET, "/animal-category/{id}").permitAll()
            .requestMatchers(HttpMethod.POST, "/animal-category").permitAll()
            .requestMatchers(HttpMethod.PATCH, "/animal-category/{id}").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/animal-category/{id}").permitAll()

            .requestMatchers(HttpMethod.GET, "/sectors").permitAll()
            .requestMatchers(HttpMethod.GET, "/sector/{id}").permitAll()
            .requestMatchers(HttpMethod.POST, "/sector").permitAll()
            .requestMatchers(HttpMethod.PATCH, "/sector/{id}").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/sector/{id}").permitAll()

            .requestMatchers(HttpMethod.GET, "/image/{name}").permitAll()
            .requestMatchers(HttpMethod.POST, "/image").permitAll()
            .requestMatchers(HttpMethod.PATCH, "/image/{name}").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/image/{name}").permitAll()

            .anyRequest().denyAll()
        );

    return http.build();
  }

  @Bean
  static MethodSecurityExpressionHandler methodSecurityExpressionHandler()
  {
    DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    return expressionHandler;
  }
}