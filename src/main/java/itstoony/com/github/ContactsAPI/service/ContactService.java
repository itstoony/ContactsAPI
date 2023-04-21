package itstoony.com.github.ContactsAPI.service;

import itstoony.com.github.ContactsAPI.dto.RegisteringContactRecord;
import itstoony.com.github.ContactsAPI.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    public Contact save(RegisteringContactRecord dto) {
        return null;
    }

    public Page<Contact> find(String name, Pageable pageable) {
        return null;
    }
}
