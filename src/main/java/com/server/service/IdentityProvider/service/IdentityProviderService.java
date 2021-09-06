package com.server.service.IdentityProvider.service;

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

import javax.validation.Valid;

import com.server.routes.*;

public interface IdentityProviderService 
{
    @PostMapping(path = Routes.LOGIN)
    public  ResponseEntity<?> getUserData(@RequestBody Map<String, String> values) throws NoSuchAlgorithmException, InvalidKeySpecException;

    @PostMapping(path = Routes.REGISTRATION)
    public ResponseEntity<?> registerUser_(@RequestBody Map<String, String> values);

    @PostMapping(path = "/error")
    public ResponseEntity<?> error(@Valid @RequestBody String error);
       
    

}
