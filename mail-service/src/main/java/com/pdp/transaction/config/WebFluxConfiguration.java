package com.pdp.transaction.config;

import com.pdp.transaction.handler.MailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableWebFlux
@Configuration
@RequiredArgsConstructor
public class WebFluxConfiguration implements WebFluxConfigurer {

    public static final String MAILS = "/api/mails";

    @Bean
    public RouterFunction<ServerResponse> singleStepPaymentRouterFunction(
            MailHandler handler) {
        return route()
                .POST(MAILS, handler::mails)
                .build();
    }
}
