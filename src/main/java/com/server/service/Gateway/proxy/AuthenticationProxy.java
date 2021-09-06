package com.server.service.Gateway.proxy;

import com.server.service.Gateway.service.AuthenticationService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@FeignClient(
        name="gateway-proxy",
        url = "http://localhost:8081")
      //  fallback = DatabaseAlternateServerComponent.class)
public interface AuthenticationProxy extends AuthenticationService {
    
}
