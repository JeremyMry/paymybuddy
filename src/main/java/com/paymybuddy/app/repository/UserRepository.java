package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(java.lang.String email);
}
