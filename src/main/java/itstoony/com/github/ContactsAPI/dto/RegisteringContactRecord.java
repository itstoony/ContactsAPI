package itstoony.com.github.ContactsAPI.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisteringContactRecord(
        @NotBlank(message = "Name is required")
        String name,
        @Email(message = "Email must be valid")
        String email,
        @Nullable
        @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s(\\d{4,5})\\-(\\d{4})$",
                message = "Invalid phone number format")
        String phone,
        @Nullable
        @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s9?(\\d{4})\\-(\\d{4})$",
                message = "Invalid cellphone number format")
        String cellPhone,
        @NotBlank(message = "Address is required")
        String address,
        @NotNull(message = "Date of birth is required")
        @PastOrPresent(message = "Date of birth cannot be in the future")
        LocalDate dateOfBirth
) {}


