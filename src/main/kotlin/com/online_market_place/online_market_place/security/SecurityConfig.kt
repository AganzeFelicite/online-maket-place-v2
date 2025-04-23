import com.online_market_place.online_market_place.security.CustomAccessDeniedHandler
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Bean
fun securityFilterChain(http: HttpSecurity, jwtAuthenticationFilter: JwtAuthenticationFilter): SecurityFilterChain {
    http
        .csrf { it.disable() }
        .authorizeHttpRequests { auth ->
            auth
                .requestMatchers(
                    "/api/v2.0/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/api/v2.0/users/verify/**",
                    "/api/v2.0/users/register/**",
                    "/actuator/**"
                ).permitAll()

                // Products
                .requestMatchers(HttpMethod.GET, "/api/v2.0/products/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/v2.0/products/**").hasRole("ADMIN")

                // Categories
                .requestMatchers(HttpMethod.GET, "/api/v2.0/categories/**").hasAnyRole("USER", "ADMIN")

                // Reviews
                .requestMatchers(HttpMethod.GET, "/api/v2.0/reviews/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/v2.0/reviews/**").hasRole("USER")

                // All other endpoints
                .anyRequest().authenticated()
        }
        .sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        .exceptionHandling { exceptionHandling ->
            exceptionHandling.accessDeniedHandler(CustomAccessDeniedHandler())
        }

    return http.build()
}
