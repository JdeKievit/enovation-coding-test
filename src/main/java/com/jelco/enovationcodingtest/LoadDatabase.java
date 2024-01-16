package com.jelco.enovationcodingtest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(AccountRepository repository) {

        return args -> {
            repository.save(new Account("marty", "Marty", "McFly", 17));
            repository.save(new Account("doc", "Emmett", "Brown", 65));
        };
    }
}