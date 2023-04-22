package itstoony.com.github.ContactsAPI.repository;

import itstoony.com.github.ContactsAPI.model.Contact;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static itstoony.com.github.ContactsAPI.contacts.utils.Utils.createContact;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class ContactRepositoryTest {

    @Autowired
    ContactRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("Should return page of contacts filtering by name")
    void findByNameTest() {
        // scenery
        Contact contact = createContact();
        String name = "John Do";

        Contact savedPerson = entityManager.persist(contact);

        // execution
        Page<Contact> result = repository.findByName(name, Pageable.ofSize(10));

        // validation
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(savedPerson);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isZero();
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return true when passed email already exists in database")
    void existsByEmailTest() {
        // scenery
        Contact contact = createContact();

        entityManager.persist(contact);

        // execution
        boolean result = repository.existsByEmail(contact.getEmail());

        // validation
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when passed email doesn't exist in database")
    void existsByInvalidEmailTest() {
        // scenery
        Contact contact = createContact();

        // execution
        boolean result = repository.existsByEmail(contact.getEmail());

        // validation
        assertThat(result).isFalse();
    }

}
