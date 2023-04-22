package itstoony.com.github.ContactsAPI.contacts.service;

import itstoony.com.github.ContactsAPI.dto.UpdatingContactRecord;
import itstoony.com.github.ContactsAPI.repository.ContactRepository;
import itstoony.com.github.ContactsAPI.dto.RegisteringContactRecord;
import itstoony.com.github.ContactsAPI.exception.BusinessException;
import itstoony.com.github.ContactsAPI.model.Contact;
import itstoony.com.github.ContactsAPI.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static itstoony.com.github.ContactsAPI.contacts.utils.Utils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ContactServiceTest {

    ContactService service;

    @MockBean
    ContactRepository repository;

    @BeforeEach
    void setUp() {
        this.service = new ContactService(repository);
    }

    @Test
    @DisplayName("Should save a contact")
    void saveTest() throws BusinessException {
        // scenery
        RegisteringContactRecord dto = createRegisteringContactDTO();
        Contact contact = createContact();

        when(repository.save(Mockito.any(Contact.class))).thenReturn(contact);
        when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
        // execution
        Contact savedContact = service.save(dto);

        // validation
        assertThat(savedContact.getName()).isEqualTo(contact.getName());
        assertThat(savedContact.getEmail()).isEqualTo(contact.getEmail());
        assertThat(savedContact.getPhone()).isEqualTo(contact.getPhone());
        assertThat(savedContact.getCellPhone()).isEqualTo(contact.getCellPhone());
        assertThat(savedContact.getAddress()).isEqualTo(contact.getAddress());
        assertThat(savedContact.getDateOfBirth()).isEqualTo(contact.getDateOfBirth());

    }

    @Test
    @DisplayName("Should throw BusinessException when trying to save contact with already saved Email")
    void savedAlreadyUnsavedEmailTest() {
        // scenery
        RegisteringContactRecord dto = createRegisteringContactDTO();

        when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
        // execution
        Exception exception = catchException(() -> service.save(dto));

        // validation
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email already saved");
    }

    @Test
    @DisplayName("Should find contact by id")
    void findByIdTest() {
        // scenery
        long id = 1L;
        Contact contact = createContact();
        contact.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(contact));

        // execution
        Optional<Contact> foundContact = service.findById(id);

        // validation
        assertThat(foundContact).isPresent();
        assertThat(foundContact.get().getName()).isEqualTo(contact.getName());
        assertThat(foundContact.get().getEmail()).isEqualTo(contact.getEmail());
        assertThat(foundContact.get().getPhone()).isEqualTo(contact.getPhone());
        assertThat(foundContact.get().getCellPhone()).isEqualTo(contact.getCellPhone());
        assertThat(foundContact.get().getAddress()).isEqualTo(contact.getAddress());
        assertThat(foundContact.get().getDateOfBirth()).isEqualTo(contact.getDateOfBirth());
        
    }
    
    @Test
    @DisplayName("Should throw an exception when trying to find inexistent contact by id")
    void findByInvalidIdTest() {
        // scenery
        long id = 1L;
        Contact contact = createContact();
        contact.setId(id);

        when(repository.findById(id)).thenReturn(Optional.empty());

        // execution
        Optional<Contact> foundContact = service.findById(id);

        // validation
        assertThat(foundContact).isEmpty();
    }

    @Test
    @DisplayName("Should find contacts filtering by name ")
    void findTest() {
        // scenery
        String name = createContact().getName();
        Contact contact = createContact();
        contact.setId(1L);


        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Contact> list = Collections.singletonList(contact);

        PageImpl<Contact> page = new PageImpl<>(list, pageRequest, 1);

        when(repository.findByName(Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(page);

        // execution
        Page<Contact> result = service.find(name, pageRequest);

        // validation
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isZero();
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }

    @Test
    @DisplayName("Should update a contact")
    void updateTest() {
        // scenery
        long id = 1L;
        Contact updatingContact = createContact();
        updatingContact.setId(id);

        UpdatingContactRecord update = createUpdatingContactDTO();

        BDDMockito.when(repository.existsByEmail(updatingContact.getEmail())).thenReturn(true);
        BDDMockito.when(repository.save(updatingContact)).thenReturn(updatingContact);

        // execution
        Contact updatedContact = service.update(updatingContact, update);

        // validation
        assertThat(updatedContact.getId()).isEqualTo(id);
        assertThat(updatedContact.getName()).isEqualTo(update.name());
        assertThat(updatedContact.getEmail()).isEqualTo(update.email());
        assertThat(updatedContact.getPhone()).isEqualTo(update.phone());
        assertThat(updatedContact.getCellPhone()).isEqualTo(update.cellPhone());
        assertThat(updatedContact.getAddress()).isEqualTo(update.address());
        assertThat(updatedContact.getDateOfBirth()).isEqualTo(update.dateOfBirth());

        verify(repository, times(1)).save(updatingContact);

    }

    @Test
    @DisplayName("Should throw an exception when trying to update an unsaved contact")
    void updateInvalidContactTest() {
        // scenery
        long id = 1L;
        Contact updatingContact = createContact();
        updatingContact.setId(id);
        UpdatingContactRecord update = createUpdatingContactDTO();

        // execution
        Throwable exception = catchThrowable(() -> service.update(updatingContact, update));

        // validation
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Can't update an unsaved contact");

        verify(repository, never()).save(updatingContact);

    }
}
