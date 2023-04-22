package itstoony.com.github.ContactsAPI.service;

import itstoony.com.github.ContactsAPI.dto.UpdatingContactRecord;
import itstoony.com.github.ContactsAPI.repository.ContactRepository;
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
        return repository.findByName(name, pageable);
    }

    public Optional<Contact> findById(long id) {
        return repository.findById(id);
    }

    public Contact update(Contact updatingContact, UpdatingContactRecord update) {
        if (!existsByEmail(updatingContact.getEmail())) throw new BusinessException("Can't update an unsaved contact");

        if (!update.name().isBlank()) updatingContact.setName(update.name());
        if (!update.email().isBlank()) updatingContact.setEmail(update.email());
        if (!update.phone().isBlank()) updatingContact.setPhone(update.phone());
        if (!update.cellPhone().isBlank()) updatingContact.setCellPhone(update.cellPhone());
        if (!update.address().isBlank()) updatingContact.setAddress(update.address());
        if (update.dateOfBirth() != null) updatingContact.setDateOfBirth(update.dateOfBirth());

        return repository.save(updatingContact);
    }

    public void delete(Contact contact) {
        repository.delete(contact);
    }

}
