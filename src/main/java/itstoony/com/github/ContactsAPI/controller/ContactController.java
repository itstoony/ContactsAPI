package itstoony.com.github.ContactsAPI.controller;

import itstoony.com.github.ContactsAPI.dto.ContactDTO;
import itstoony.com.github.ContactsAPI.dto.RegisteringContactRecord;
import itstoony.com.github.ContactsAPI.dto.UpdatingContactRecord;
import itstoony.com.github.ContactsAPI.model.Contact;
import itstoony.com.github.ContactsAPI.service.ContactService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/contact")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ContactController {

    private final ContactService service;

    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ContactDTO> save(@RequestBody @Valid RegisteringContactRecord dto) {
        log.info("Saving contact with Email: {}", dto.email());

        Contact savedContact = service.save(dto);
        ContactDTO savedDTO = modelMapper.map(savedContact, ContactDTO.class);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(savedDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ContactDTO>> listAll(@PathParam("name") String name, Pageable pageable) {
        log.info("Listing all contacts");

        Page<Contact> page = service.find(name, pageable);
        List<ContactDTO> listDTO = page.stream().map(contact -> modelMapper.map(contact, ContactDTO.class)).toList();

        return ResponseEntity.ok(new PageImpl<>(listDTO, pageable, page.getTotalElements()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> findById(@PathVariable(name = "id") Long id) {
        log.info("Finding contact with id: {}", id);

        Contact foundContact = service.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found by id: " + id));
        ContactDTO dto = modelMapper.map(foundContact, ContactDTO.class);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> update(@PathVariable(name = "id") Long id,
                                             @RequestBody UpdatingContactRecord update) {
        log.info("Updating contact with id: {}", id);

        Contact updatingContact = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Updating contact not found"));

        Contact updatedContact = service.update(updatingContact, update);

        ContactDTO dto = modelMapper.map(updatedContact, ContactDTO.class);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        log.info("deleting contact with id: {}", id);

        Contact contact = service.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found by id: " + id));
        service.delete(contact);

        return ResponseEntity.noContent().build();
    }
}
