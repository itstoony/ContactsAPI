package itstoony.com.github.ContactsAPI.contacts.utils;

import itstoony.com.github.ContactsAPI.dto.RegisteringContactRecord;
import itstoony.com.github.ContactsAPI.model.Contact;

import java.time.LocalDate;

public class Utils {

    public static Contact createContact() {
        return Contact.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .phone("21 1234-5678")
                .cellPhone("(11) 91234-5678")
                .address("123 Main St")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .build();
    }

    public static RegisteringContactRecord createRegisteringContactDTO() {
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
