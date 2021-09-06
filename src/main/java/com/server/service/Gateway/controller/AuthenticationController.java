package com.server.service.Gateway.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.algorithms.Algorithm;
import com.netflix.discovery.EurekaClient;
import com.netflix.ribbon.proxy.annotation.Http;
import com.server.routes.*;
import com.server.service.Gateway.model.User;
import com.server.service.Gateway.proxy.AuthenticationProxy;
import com.server.service.Gateway.service.AuthenticationService;
import com.server.service.IdentityProvider.client.Authentication;
import com.server.service.Gateway.model.User;
import com.server.service.Gateway.model.UserRepository;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/user")
public class AuthenticationController implements AuthenticationService {

    private String token = "none";

    @Lazy
    private EurekaClient discoveryClient;

    private final UserRepository userRepository;

    private AuthenticationProxy authenticationProxy;

    private final String role = "user";

    private static Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @PostMapping(path = Routes.LOGIN)
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> values)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        Authentication authentication = new Authentication();

        List<String> resource = new ArrayList<String>();

        try {
            String userID = values.get("userId").trim();
            String email = values.get("email").trim();
            String password = values.get("password").trim();
            String role = values.get("role").trim();
            
            boolean success = authentication.createToken(userID, email, password, role);

            if (!success)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

            token = authentication.getToken();
            resource.add(token);

        } catch (

        NullPointerException e) {
            LOGGER.info("error " + e);
            System.out.println(e);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @Override
    @PostMapping(path = Routes.REGISTRATION)
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> values)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        Authentication authentication = new Authentication();
        String email = "";
     
        try {
            email = values.get("email").trim();
            this.userRepository.findByEmail(email);

            String firstName = values.get("firstName").trim();
            String userName = values.get("userName").trim();
            String surname = values.get("surname").trim();
            String password = values.get("password").trim();

            String hash = authentication.generateHash(password); // Hash password
            
            // save details in database
            this.userRepository.save(new User(firstName, surname, userName, email, hash, role));
        } catch (Exception ex) {
          
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);

        }

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

}
