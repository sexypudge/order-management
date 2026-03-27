    package org.example.ordermanagement.config;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
    import org.springframework.security.oauth2.jwt.JwtDecoder;
    import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
    import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
    import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
    import org.springframework.security.web.SecurityFilterChain;

    import javax.crypto.spec.SecretKeySpec;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Value("${jwt.signerKey}")
        private String signerKey = "";

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(HttpMethod.POST, "/api/auth/**", "/api/users").permitAll()

                            // 2. Quyền của ADMIN (Quản lý role, hệ thống)
                            .requestMatchers("/api/role/admin/**").hasRole("ADMIN")


                            // 3. Quyền của STAFF & ADMIN (Quản lý đơn hàng)
                            .requestMatchers(HttpMethod.POST, "/orders").hasAnyRole("ADMIN", "STAFF")
                            .requestMatchers(HttpMethod.GET, "/api/orders/all").hasAnyRole("STAFF", "ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/orders/*/status").hasAnyRole("STAFF", "ADMIN")

                            //  Quyền của CUSTOMER (Tạo đơn)
                            .requestMatchers(HttpMethod.POST, "/api/orders/**").hasRole("CUSTOMER")
                            .requestMatchers(HttpMethod.GET, "/api/orders/my-orders").hasAnyRole("CUSTOMER")

                            //  Quyền chung (Xem chi tiết, Search đơn hàng)
                            .requestMatchers(HttpMethod.GET, "/api/orders/**").authenticated()

                            .anyRequest().authenticated()
                    )
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwtConfigurer -> jwtConfigurer
                                    .decoder(jwtDecoder())
                                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                            )
                    );
            return http.build();
        }

        @Bean
        JwtDecoder jwtDecoder() {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");
            return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS256).build();
        }

        @Bean
        JwtAuthenticationConverter jwtAuthenticationConverter() {
            JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
            converter.setAuthorityPrefix("ROLE_");

            converter.setAuthoritiesClaimName("scope");

            JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
            jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
            return jwtConverter;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }