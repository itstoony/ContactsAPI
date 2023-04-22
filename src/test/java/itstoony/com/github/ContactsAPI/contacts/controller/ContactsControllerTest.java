package itstoony.com.github.ContactsAPI.contacts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import itstoony.com.github.ContactsAPI.dto.RegisteringContactRecord;
import itstoony.com.github.ContactsAPI.model.Contact;
import itstoony.com.github.ContactsAPI.service.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

import static itstoony.com.github.ContactsAPI.contacts.utils.Utils.createContact;
import static itstoony.com.github.ContactsAPI.contacts.utils.Utils.createRegisteringContactDTO;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest
@ActiveProfiles("test")
class ContactsControllerTest {

    static String CONTACTS_API = "/contact";

    @Autowired
    MockMvc mvc;

    @MockBean
    ContactService service;

    @Test
    @DisplayName("Should save a contact")
    void saveTest() throws Exception {
        // scenery
        RegisteringContactRecord dto = createRegisteringContactDTO();
        Contact savedContact = createContact();
        savedContact.setId(1L);

        String json = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writeValueAsString(dto);

        BDDMockito.given(service.save(dto)).willReturn(savedContact);

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CONTACTS_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // validation
        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(savedContact.getId()))
                .andExpect(jsonPath("name").value(dto.name()))
                .andExpect(jsonPath("email").value(dto.email()))
                .andExpect(jsonPath("phone").value(dto.phone()))
                .andExpect(jsonPath("cellPhone").value(dto.cellPhone()))
                .andExpect(jsonPath("address").value(dto.address()))
                .andExpect(jsonPath("dateOfBirth").value(dto.dateOfBirth().toString()));

    }

    @Test
    @DisplayName("Should not save invalid Contact")
    void saveInvalidContactTest() throws Exception {
        // test
        RegisteringContactRecord dto = new RegisteringContactRecord("", "", "", "", "", null);

        String json = new ObjectMapper().writeValueAsString(dto);

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CONTACTS_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        // validation
        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(5)));
    }

    @Test
    @DisplayName("Should list all contacts")
    void listAllTest() throws Exception {
        // scenery
        Contact contact = createContact();
        contact.setId(1L);

        BDDMockito.given( service.find(Mockito.any(String.class), Mockito.any(Pageable.class)) )
                .willReturn(new PageImpl<>(Collections.singletonList(contact), Pageable.ofSize(100), 1) );

        String queryString = String.format("?name=%s&page=0&size=10",
                contact.getName());

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CONTACTS_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        // validation
        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("content.[0].name").value(contact.getName()))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(10))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    @Test
    @DisplayName("Should find a contact by id")
    void findByIdTest() throws Exception {
        // scenery
        long id = 1L;
        Contact contact = createContact();
        contact.setId(id);

        BDDMockito.given(service.findById(id)).willReturn(Optional.of(contact));

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CONTACTS_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON);

        // validation
        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value(contact.getName()))
                .andExpect(jsonPath("email").value(contact.getEmail()))
                .andExpect(jsonPath("phone").value(contact.getPhone()))
                .andExpect(jsonPath("cellPhone").value(contact.getCellPhone()))
                .andExpect(jsonPath("address").value(contact.getAddress()))
                .andExpect(jsonPath("dateOfBirth").value(contact.getDateOfBirth().toString()));
    }

    @Test
    @DisplayName("Should return 404 when trying to find an invalid contact by id")
    void findByInvalidIdTest() throws Exception {
        // scenery
        long id = 1L;

        // execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CONTACTS_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON);

        // validation
        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

}
