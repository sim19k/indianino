package com.server.service.IdentityProvider.client;

import java.util.Date;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.*;
import java.time.Instant;
import java.math.BigInteger;
import java.security.*;
import java.time.temporal.ChronoUnit;

import com.auth0.jwt.*;//JWTVerifier;
import com.auth0.jwt.algorithms.*;//Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Authentication {
    private String token;

    private void setToken(String token_) {
        token = token_;
    }

    public String getToken() {
        return token;
    }

    public boolean checkExpiry(String token)
    {
        Date date = new Date();
        Instant inst = date.toInstant();
        inst.plus(2, ChronoUnit.HOURS);
        Date new_date = Date.from(inst);

        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build(); // Reusable verifier instance
        Date date_ = verifier.verify(token).getIssuedAt();

        System.out.println(date_.getTime());
        return true;

    }

    public boolean createToken(String id, String email, String password, String role) {
        Date date = new Date();
        Instant inst = date.toInstant();
        inst.plus(2, ChronoUnit.HOURS);
        Date new_date = Date.from(inst);

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");

            String token_ = JWT.create().withClaim("id", id).withClaim("email", email).withIssuer("auth0")
                    .withClaim("password", password).withClaim("role", role).withExpiresAt(new_date).sign(algorithm);

            // Builder build = JWT.create();

            setToken(token_);

        } catch (JWTCreationException exception) {
            return false;
        }
        return true;
    }

    public boolean verifyToken(String token) {
        // Get token from database
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("auth0").build(); // Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return false;
        }
        return true;
    }

    public String generateHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 2000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
     
    private byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
     
    private String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
    
    public boolean validatePassword(String originalPassword, String storedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
    
}
