package com.in28minutes.microservices.camelmicroservicea.routes.a;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyFirstTimerRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        // queue | timer
        // transformation
        // database | log
        // Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]
        from("timer:first-timer") // queue | null
                // .transform().constant("My constant Message")
                .transform().constant("Time is now" + LocalDateTime.now())
                .to("log:first-timer"); // database



    }
}
