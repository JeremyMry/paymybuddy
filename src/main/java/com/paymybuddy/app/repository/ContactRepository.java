package com.paymybuddy.app.repository;

import com.paymybuddy.app.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("FROM Contact c where c.creator = :userId")
    List<Contact> findAllByCurrentUser(@Param("userId") Long userId);

    Optional<Contact> findByEmail(String email);
}
