package com.infinum.academy.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { }
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeRequests {
                authorize(HttpMethod.POST, "/car", hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER"))
                authorize(HttpMethod.GET, "/car", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "/car/{id}", hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER"))
                authorize(HttpMethod.POST, "/car/{id}/checkup", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "/car/{id}/checkup", hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER"))
                authorize(HttpMethod.DELETE, "/car/{id}", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.DELETE, "/car/{id}/checkup/{checkupId}", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "/checkup/analytics", permitAll)
                authorize(anyRequest, authenticated)
            }
            oauth2ResourceServer {
                jwt {}
            }
        }
        return http.build()
    }
}
