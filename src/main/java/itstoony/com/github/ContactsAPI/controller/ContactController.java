package itstoony.com.github.ContactsAPI.controller;

import itstoony.com.github.ContactsAPI.dto.ContactDTO;
import itstoony.com.github.ContactsAPI.dto.RegisteringContactRecord;
import itstoony.com.github.ContactsAPI.model.Contact;
import itstoony.com.github.ContactsAPI.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping("/contact")
@RestController
@RequiredArgsConstructor
public class ContactController {

    private final ContactService service;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ContactDTO> save(@RequestBody @Valid RegisteringContactRecord dto) {
        Contact savedContact = service.save(dto);
        ContactDTO savedDTO = modelMapper.map(savedContact, ContactDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(savedDTO);
    }
}
