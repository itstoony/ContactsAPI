package itstoony.com.github.ContactsAPI.contacts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import itstoony.com.github.ContactsAPI.model.Contact;
import itstoony.com.github.ContactsAPI.dto.RegisteringContactRecord;
import itstoony.com.github.ContactsAPI.service.ContactService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

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

    private static Contact createContact() {
        return Contact.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .phone("21 1234-5678")
                .cellPhone("(11) 91234-5678")
                .address("123 Main St")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .build();
    }

    private static RegisteringContactRecord createRegisteringContactDTO() {
        return new RegisteringContactRecord(
                "John Doe",
                "johndoe@example.com",
                "21 1234-5678",
                "(11) 91234-5678",
                "123 Main St",
                LocalDate.of(1990, 5, 15)
        );

    }

}