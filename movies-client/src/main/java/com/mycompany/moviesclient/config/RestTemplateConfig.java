package com.mycompany.moviesclient.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Value("${http.client.ssl.trust-store}")
  private Resource trustStore;

  @Value("${http.client.ssl.trust-store-password}")
  private char[] trustStorePassword;

  @Bean
  RestTemplate restTemplate() throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException,
      KeyManagementException, UnrecoverableKeyException, KeyStoreException {
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(new FileInputStream(trustStore.getFile()), trustStorePassword);

    SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy())
        .loadKeyMaterial(keyStore, trustStorePassword).build();

    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);

    HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
    ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
    return new RestTemplate(requestFactory);
  }

}