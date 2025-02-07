// package com.bookstore.book_store.Auth0;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.oauth2.client.registration.ClientRegistration;
// import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
// import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
// import org.springframework.security.oauth2.core.AuthorizationGrantType;
// import org.springframework.security.web.SecurityFilterChain;


// @Configuration
// public class SecurityConfig {

//     @Value("${oauth2.domain}")
//     private String domain;

//     @Value("${oauth2.clientId}")
//     private String clientId;

//     @Value("${oauth2.clientSecret}")
//     private String clientSecret;

//     @Value("${frontend.url}")
//     private String frontendUrl;

//     @Bean
//     public ClientRegistrationRepository clientRegistrationRepository() {
//         return new InMemoryClientRegistrationRepository(this.auth0ClientRegistration());
//     }

//     private ClientRegistration auth0ClientRegistration() {
//         return ClientRegistration.withRegistrationId("auth0")
//             .clientId(clientId)
//             .clientSecret(clientSecret)
//             .issuerUri("https://" + domain + "/")
//             .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//             .authorizationUri("https://" + domain + "/authorize")
//             .tokenUri("https://" + domain + "/oauth/token")
//             .jwkSetUri("https://" + domain + "/.well-known/jwks.json")
//             .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
//             .userInfoUri("https://" + domain + "/userinfo")
//             .userNameAttributeName("sub")
//             .clientName("Auth0")
//             .build();
//     }

//     @Bean
//     public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//         return http
//             .authorizeHttpRequests(authorize -> authorize
//                 .requestMatchers("/", "/images/**").permitAll()
//                 .anyRequest().authenticated()
//             )
//             .oauth2Login(oauth2 -> oauth2
//                 .defaultSuccessUrl(frontendUrl, true)
//             )
//             .build();
//     }
// }