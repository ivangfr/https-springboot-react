package com.ivanfranchin.moviesshell.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.autoconfigure.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class MoviesApiClientConfig {

  @Bean
  MoviesApiClient moviesApiClient(
      RestClient.Builder builder,
      RestClientSsl ssl,
      @Value("${movies-api.url}") String moviesApiUrl) {
    RestClient restClient =
        builder.baseUrl(moviesApiUrl).apply(ssl.fromBundle("my-client-bundle")).build();
    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(MoviesApiClient.class);
  }
}
