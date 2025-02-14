package com.pdp.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication(scanBasePackages = {"com.pdp.transaction"})
public class MailApplication {

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(MailApplication.class, args);
    }
}
