package com.server.service.Gateway.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;

import com.server.routes.*;

public interface AuthenticationService 
{
    @PostMapping(path = Routes.LOGIN)
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> values) throws NoSuchAlgorithmException, InvalidKeySpecException;

    @PostMapping(path = Routes.REGISTRATION) 
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> values) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
