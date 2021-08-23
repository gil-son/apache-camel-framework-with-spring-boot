package com.in28minutes.microservices.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class RestApiConsumerRouter extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		
		restConfiguration().host("localhost").port(8001); // Consumer api from project b
		
		from("timer:rest-api-consumer?period=10000")
		.setHeader("from", () -> "EUR")
		.setHeader("to", () -> "INR")
		.log("${body}")
		.to("rest:get:/currency-exchange/from/{from}/to/{to}")
		.log("${body}");

	}

}
