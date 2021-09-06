package com.server.service.IdentityProvider.proxy;

import com.server.service.IdentityProvider.service.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@FeignClient(
        name="identity-provider-proxy",
        url = "http://localhost:8082")
      //  fallback = DatabaseAlternateServerComponent.class)
public interface IdentityProviderProxy extends IdentityProviderService {
    
}
