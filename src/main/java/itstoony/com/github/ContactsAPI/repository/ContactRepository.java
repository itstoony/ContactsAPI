package itstoony.com.github.ContactsAPI.repository;

import itstoony.com.github.ContactsAPI.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT c FROM Contact c WHERE lower(c.name) like lower(concat('%', :name, '%'))")
    Page<Contact> findByName(@Param("name") String name, Pageable pageable);

}
