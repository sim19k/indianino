package com.server.service.IdentityProvider.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityRepository extends MongoRepository<User, String> {
    public User findByEmail(final String email);
}
