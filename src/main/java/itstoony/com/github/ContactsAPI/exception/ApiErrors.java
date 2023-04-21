package itstoony.com.github.ContactsAPI.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ApiErrors {

    private final List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(e -> this.errors.add(e.getDefaultMessage()));
    }

    public ApiErrors(ResponseStatusException ex) {
        this.errors = Collections.singletonList(ex.getReason());
    }

}
