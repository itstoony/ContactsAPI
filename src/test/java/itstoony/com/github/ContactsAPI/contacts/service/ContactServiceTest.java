package itstoony.com.github.ContactsAPI.contacts.service;

import itstoony.com.github.ContactsAPI.repository.ContactRepository;
import itstoony.com.github.ContactsAPI.dto.RegisteringContactRecord;
import itstoony.com.github.ContactsAPI.exception.BusinessException;
import itstoony.com.github.ContactsAPI.model.Contact;
import itstoony.com.github.ContactsAPI.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static itstoony.com.github.ContactsAPI.contacts.utils.Utils.createContact;
import static itstoony.com.github.ContactsAPI.contacts.utils.Utils.createRegisteringContactDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;

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
}
