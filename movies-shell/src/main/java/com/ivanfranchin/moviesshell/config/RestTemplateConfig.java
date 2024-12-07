package com.ivanfranchin.moviesshell.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Configuration
public class RestTemplateConfig {

    @Value("${http.client.ssl.trust-store}")
    private String trustStore;

    @Value("${http.client.ssl.trust-store-password}")
    private char[] trustStorePassword;

    @Bean
    public RestTemplate restTemplate() throws NoSuchAlgorithmException, CertificateException, IOException,
            KeyManagementException, UnrecoverableKeyException, KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream in = this.getClass().getResourceAsStream(trustStore);
        keyStore.load(in, trustStorePassword);

        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(null, new TrustAllStrategy())
                .loadKeyMaterial(keyStore, trustStorePassword)
                .build();

        Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(URIScheme.HTTPS.getId(), new SSLConnectionSocketFactory(sslContext))
                .register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory())
                .build();

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
                .setConnectionManagerShared(true)
                .build();

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }
}