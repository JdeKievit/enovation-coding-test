package com.jelco.enovationcodingtest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
class AccountModelAssembler implements RepresentationModelAssembler<Account, EntityModel<Account>> {

    @Override
    public EntityModel<Account> toModel(Account account) {
        return EntityModel.of(account,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class)
                        .getAccount(account.getUsername())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AccountController.class)
                        .getAllAccounts()).withRel("getAllAccounts"));
    }
}