package com.jelco.enovationcodingtest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
class AccountController {

    private final AccountRepository repository;

    private final AccountModelAssembler assembler;

    AccountController(AccountRepository repository, AccountModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/account/{username}")
    EntityModel<Account> getAccount(@PathVariable String username) {
        Account employee = repository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException(username));
        return assembler.toModel(employee);
    }

    @PutMapping("/account/{username}")
    ResponseEntity<?> putAccount(@PathVariable String username, @Valid @RequestBody Account accountDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(400).body("Error validating account: " + bindingResult.getAllErrors());
        }
        
        Account account = repository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException(username));
        account.setUsername(accountDto.getUsername());
        account.setFirstName(accountDto.getFirstName());
        account.setLastName(accountDto.getLastName());
        account.setAge(accountDto.getAge());
        account = repository.save(account);

        EntityModel<Account> entityModel = assembler.toModel(account);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PostMapping("/account")
    ResponseEntity<?> postAccount(@Valid @RequestBody Account account, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(400).body("Error validating account: " + bindingResult.getAllErrors());
        }

        EntityModel<Account> entityModel = assembler.toModel(repository.save(account));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @GetMapping("/accounts")
    CollectionModel<EntityModel<Account>> getAllAccounts() {
        List<EntityModel<Account>> accounts = repository.findAll().stream().map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(accounts, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class)
                .getAllAccounts()).withSelfRel());
    }
}