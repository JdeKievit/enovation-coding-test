package com.jelco.enovationcodingtest;

class AccountNotFoundException extends RuntimeException {

    AccountNotFoundException(String username) {
        super("Could not find account with username " + username);
    }
}