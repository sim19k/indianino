package com.server.service.IdentityProvider.restclient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.server.interceptor.*;
import com.server.service.IdentityProvider.model.*;
import java.util.*;

public class RestClient {
  private static final String LOGIN_URL = "http://localhost:8761/user/login";
  private static final String REGISTRATION_URL = "http://localhost:8761/user/registration";
  private static final String ERROR = "http://localhost:4200/identity/error";

  private String token = "";

  private void setToken(String token_) {
    token = token_;
  }

  public String getToken() {
    return token;
  }

  public boolean sendCredentials(User user) {
    try {
      RestTemplateIntercepter restTemplate = new RestTemplateIntercepter();
      RestTemplate restTemplate_ = restTemplate.restTemplate();
      ResponseEntity<?> entity = restTemplate_.postForEntity(LOGIN_URL, user, Object.class);

      if (entity.getStatusCode().value() == 200) {
        setToken(entity.getBody().toString());
        System.out.println("TOKEN " + entity.getBody().toString());
        return true;
      }

    } catch (Exception e) {
      System.out.println("ERROR " + e);
      return false;
    }

    return false;
  }

  public boolean storeCredentials(User user) {
    try {
      RestTemplateIntercepter restTemplate = new RestTemplateIntercepter();
      RestTemplate restTemplate_ = restTemplate.restTemplate();
      ResponseEntity<?> entity = restTemplate_.postForEntity(REGISTRATION_URL, user, Object.class);

      if (entity.getStatusCode().value() == 200) {
        return true;
      }

    } catch (Exception e) {
      System.out.println("ERROR " + e);
      return false;
    }

    return false;
  }

  public void error() {
    Map<String, String> params = new HashMap<String, String>();
    params.put("error", "error");

    RestTemplateIntercepter restTemplate = new RestTemplateIntercepter();
    RestTemplate restTemplate_ = restTemplate.restTemplate();
    restTemplate_.postForEntity(ERROR, "error", String.class, "error");
  }

}
