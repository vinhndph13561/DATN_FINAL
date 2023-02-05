package com.example.demo.config;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.resolver.DefaultAddressResolverGroup;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
	
	@Bean
    public WebClient loadBalancedWebClientBuilder() throws SSLException {
//		HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
		
//     return WebClient.builder()
//    		 	.clientConnector(new ReactorClientHttpConnector(httpClient))
//		        .defaultHeader("token", "f1e25a8c-6ec0-11ed-b62e-2a5743127145")
//		        .build();
		SslContext context = SslContextBuilder.forClient()
			    .trustManager(InsecureTrustManagerFactory.INSTANCE)
			    .build();
			                
			HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(context));

			return WebClient
			                    .builder()
			                    .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

	
}
