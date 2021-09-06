package com.server.service.IdentityProvider.controller;

import com.server.routes.Routes;
import com.server.service.IdentityProvider.service.IdentityProviderService;

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
import com.server.routes.*;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

import com.server.service.IdentityProvider.client.Authentication;
import com.server.service.IdentityProvider.model.User;
import com.server.service.IdentityProvider.model.IdentityRepository;
import com.server.service.IdentityProvider.proxy.IdentityProviderProxy;
import com.server.service.IdentityProvider.restclient.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/identity")
public class IdentityProviderController implements IdentityProviderService {

    private IdentityProviderProxy identityProviderProxy;

    private final IdentityRepository identityRepository;

    private final String role = "user";

    private static User user;

    private static Logger LOGGER = LoggerFactory.getLogger(IdentityProviderController.class);

    private String token = "none";
    @Lazy
    private EurekaClient discoveryClient;

    public IdentityProviderController(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    @Override
    @PostMapping(path = "/checkCredentials")
    public ResponseEntity<?> getUserData(@RequestBody Map<String, String> values)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        Authentication authentication = new Authentication();
        RestClient restclient = new RestClient();
        boolean validPassword = false;
        List<String> resource = new ArrayList<String>();

        try {
            String email = values.get("email").trim();
            String password = values.get("password").trim();
            user = this.identityRepository.findByEmail(email);

            System.out.println(user.getEmail());
            token = values.get("authorization").trim();

            // pbkdf2 to validate password validPassword =
            validPassword = authentication.validatePassword(password, user.getPassword().toString());

            if (!validPassword) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
            }
        } catch (NullPointerException e) {
            LOGGER.info("error " + e);
            System.out.println(e);
        }

        boolean res = restclient.sendCredentials(user);

        if (!res)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);

        token = restclient.getToken();
        resource.add(token);
        LOGGER.info("THIS RESPONSE SHALL BE CONSUMED");
        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @Override
    @PostMapping(path = "/storeCredentials")
    public ResponseEntity<?> registerUser_(@RequestBody Map<String, String> values) {
        Authentication authentication = new Authentication();
        RestClient restclient = new RestClient();
   
        try {
            String firstName = values.get("firstName").trim();
            String userName = values.get("userName").trim();
            String surname = values.get("surname").trim();
            String email = values.get("email").trim();
            String password = values.get("password").trim();
            String token = values.get("authorization").trim();

            if (!token.equals("none")) {
                Boolean exp = authentication.checkExpiry(token);
                
                if(exp){
                    authentication.createToken(user.getUserId(), user.getEmail(), user.getPassword(), role);
                    token = authentication.getToken();
                }

                // Verify Token
                boolean validToken = authentication.verifyToken(token);
                if (!validToken)
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
            }

            User user = new User(firstName, surname, userName, email, password, role);
            restclient.storeCredentials(user);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @Override
    @PostMapping(path = "/error")
    public ResponseEntity<?> error(@Valid @RequestBody String error) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

}
