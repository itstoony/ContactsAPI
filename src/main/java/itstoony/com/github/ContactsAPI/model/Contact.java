package itstoony.com.github.ContactsAPI.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contact")
public class Contact {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @Nullable
    @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s(\\d{4,5})\\-(\\d{4})$",
            message = "Invalid phone number format")
    @Column(name = "phone")
    private String phone;

    @Nullable
    @Pattern(regexp = "^\\(?(\\d{2})\\)?\\s9?(\\d{4})\\-(\\d{4})$",
            message = "Invalid cellphone number format")
    @Column(name = "cellphone")
    private String cellPhone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;

}
