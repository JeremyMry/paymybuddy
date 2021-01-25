package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Integer> {
}
