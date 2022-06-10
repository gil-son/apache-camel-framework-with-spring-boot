package com.in28minutes.microservices.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class KafkaSenderRouter extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		
		/*
		from("file:files/xml")
		.log("${body}")
		.to("activemq:my-activemq-queue");
		*/


		from("file:files/json")
		.log("${body}")
		.to("kafka:myKafkaTopic");


	}

}
