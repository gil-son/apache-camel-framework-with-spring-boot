package com.in28minutes.microservices.camelmicroserviceb.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.in28minutes.microservices.camelmicroserviceb.CurrencyExchange;


@Component 
public class ActiveMqReceiverRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		//JSON
		//CurrencyExchange
		// {"id": 1000,"from": "USD","to": "INR","conversionMultiple": 70}
		from("activemq:my-activemq-queue")
		.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
		.to("log:received-message-from-active-mq");
	}
	
}
