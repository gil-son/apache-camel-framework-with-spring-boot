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

		/*
		from("file:files/json")
		.log("${body}")
		.to("kafka:myKafkaTopic");
		*/

		//timer

		from("timer:active-mq-timer?period=10000")
				.doTry()
					.log("Initial")
					.transform().constant("My message for Active MQ")
					.log("${body}")
					.to("activemq:my-activemq-queue")
				.doCatch(UncategorizedJmsException.class, JMSException.class)
					.to("direct:catch")
//				.doFinally()
//					.to("direct:finally")
				.end();


		from("direct:catch")
				.log(" Catch: Error queue connection")
				.end();


		from("direct:finally")
				.log("Finally: Error queue connection")
				.end();

		//queue


	}

}
