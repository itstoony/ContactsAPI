package itstoony.com.github.ContactsAPI.service;

import itstoony.com.github.ContactsAPI.contacts.repository.ContactRepository;
import itstoony.com.github.ContactsAPI.dto.RegisteringContactRecord;
import itstoony.com.github.ContactsAPI.exception.BusinessException;
import itstoony.com.github.ContactsAPI.model.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository repository;


    public Contact save(RegisteringContactRecord dto) {

        if (existsByEmail(dto.email())) throw new BusinessException("Email already saved");

        Contact contact = createContact(dto);

        return repository.save(contact);
    }

    private boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    private Contact createContact(RegisteringContactRecord dto) {
        return Contact.builder()
                .name(dto.name())
                .email(dto.email())
                .phone(dto.phone())
                .cellPhone(dto.cellPhone())
                .address(dto.address())
                .dateOfBirth(dto.dateOfBirth())
                .build();
    }

    public Page<Contact> find(String name, Pageable pageable) {
        return null;
    }

    public Optional<Contact> findById(long id) {
        return Optional.empty();
    }

}
