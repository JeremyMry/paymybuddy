package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends CrudRepository<Contact, Integer> {

    @Query("FROM Contact c where c.user.id = :userId")
    List<Contact> findAllByCurrentUser(@Param("userId") Integer userId);
}
