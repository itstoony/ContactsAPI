package itstoony.com.github.ContactsAPI.contacts.repository;

import itstoony.com.github.ContactsAPI.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    boolean existsByEmail(String email);

}
