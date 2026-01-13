package com.ivanfranchin.moviesshell.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class MovieApiClientConfig {

    @Value("${movies-api.url}")
    private String moviesApiUrl;

    @Bean
    MovieApiClient movieApiClient(RestClient.Builder builder, RestClientSsl ssl) {
        RestClient restClient = builder.baseUrl(moviesApiUrl)
                .apply(ssl.fromBundle("my-client-bundle"))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(MovieApiClient.class);
    }
}