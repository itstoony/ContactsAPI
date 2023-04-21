package itstoony.com.github.ContactsAPI.service;

import itstoony.com.github.ContactsAPI.dto.RegisteringContactRecord;
import itstoony.com.github.ContactsAPI.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactService {

    public Contact save(RegisteringContactRecord dto) {
        return null;
    }

    public Page<Contact> find(String name, Pageable pageable) {
        return null;
    }

    public Optional<Contact> findById(long id) {
        return Optional.empty();
    }

}
