package com.openclassrooms.mddapi.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.lang.reflect.Method;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(SecurityConfigurationTest.SecurityConfig.class)
class SecurityConfigurationTest {

  @Configuration(proxyBeanMethods = false)
  static class SecurityConfig {
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
      return Mockito.mock(JwtAuthenticationFilter.class);
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
      return Mockito.mock(AuthenticationProvider.class);
    }

    @Bean
    SecurityConfiguration securityConfiguration(JwtAuthenticationFilter filter, AuthenticationProvider provider) {
      return new SecurityConfiguration(filter, provider);
    }
  }

  @Autowired
  SecurityConfiguration configuration;

  @Autowired
  JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  AuthenticationProvider authenticationProvider;

  @Test
  void writeUnauthorizedSets401Status() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    StringWriter output = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(output));

    Method method = SecurityConfiguration.class.getDeclaredMethod(
      "writeUnauthorized",
      HttpServletRequest.class,
      HttpServletResponse.class,
      org.springframework.security.core.AuthenticationException.class
    );
    method.setAccessible(true);
    method.invoke(configuration, request, response, null);

    verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    assert output.toString().contains("Unauthorized request");
  }

  @Test
  void securityFilterChainBuildsWithJwtFilterAndAuthProvider() throws Exception {
    HttpSecurity http = Mockito.mock(HttpSecurity.class, Mockito.RETURNS_DEEP_STUBS);
    DefaultSecurityFilterChain expectedFilterChain = Mockito.mock(DefaultSecurityFilterChain.class);
    when(http.build()).thenReturn(expectedFilterChain);

    SecurityFilterChain filterChain = configuration.securityFilterChain(http);

    assertSame(expectedFilterChain, filterChain);
    verify(http).csrf(any());
    verify(http).build();
  }
}
