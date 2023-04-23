package itstoony.com.github.ContactsAPI.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NotEmpty(message = "Login must not be empty")
    private String login;
    @NotEmpty(message = "Password must not be empty")
    private String password;

}
