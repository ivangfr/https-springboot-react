package com.ivanfranchin.moviesshell;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ivanfranchin.moviesshell.client.MoviesApiClient;

// Loads the full Spring Boot application context, exercising the entire auto-configuration
// chain — in particular MoviesApiClientConfig (RestClient, SSL bundle, movies-api.url,
// HttpServiceProxyFactory). MoviesCommandsTests uses a @ShellTest slice that never touches
// any of that, so a misconfigured bean there would go undetected without this test.
@SpringBootTest
class MoviesShellApplicationTests {

  // Replaces the real MoviesApiClient bean produced by MoviesApiClientConfig with a mock.
  // This keeps the test focused on context wiring rather than HTTP behaviour, and avoids
  // any accidental network calls should startup logic ever change.
  @MockitoBean MoviesApiClient moviesApiClient;

  @Test
  public void contextLoads() {}
}
