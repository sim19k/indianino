package com.server.service.IdentityProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class IdentityProviderServer {

  /*  @Autowired
    protected AccountRepository accountRepository;*/

    /**
     * Run the application using Spring Boot and an embedded servlet engine.
     * 
     * @param args Program arguments - ignored.
     */
    public static void main(String[] args) {
        System.setProperty("spring.config.name", "identity-provider");
        SpringApplication.run(IdentityProviderServer.class, args);
    }
}