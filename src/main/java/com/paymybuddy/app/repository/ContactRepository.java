package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Integer> {
}
