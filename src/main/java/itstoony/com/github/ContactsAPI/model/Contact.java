package itstoony.com.github.ContactsAPI.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @Nullable
    @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s(\\d{4,5})\\-(\\d{4})$",
            message = "Invalid phone number format")
    private String phone;

    @Nullable
    @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s9?(\\d{4})\\-(\\d{4})$",
            message = "Invalid cellphone number format")
    private String cellPhone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;

}
