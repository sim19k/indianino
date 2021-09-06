package com.server.interceptor;

import java.io.Console;
import java.io.IOException;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class RestIntercepter implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        ClientHttpResponse response = execution.execute(request, body);
        request.getHeaders().set("Content-Type", "application/json");

        if (response.getStatusCode().value() == 401) {
         //   response.getHeaders().set(headerName, response.getStatusCode().value();
      
        }

        // System.out.println("STATUS CODE " + response.getStatusCode());

        return response;
    }

}
