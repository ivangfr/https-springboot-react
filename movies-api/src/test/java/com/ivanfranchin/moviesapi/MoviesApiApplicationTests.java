package com.ivanfranchin.moviesapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Ensures the Spring Boot application context loads successfully.
 *
 * <p>Importance:
 *
 * <ul>
 *   <li>Catches bean creation failures, misconfigured components, and auto-configuration issues
 *       early
 *   <li>Runs without a web server, making it fast and lightweight
 *   <li>Serves as a minimal smoke test for application startup health
 * </ul>
 */
@SpringBootTest
class MoviesApiApplicationTests {

  @Test
  public void contextLoads() {}
}
