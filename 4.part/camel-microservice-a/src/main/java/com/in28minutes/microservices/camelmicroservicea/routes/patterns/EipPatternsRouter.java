package com.in28minutes.microservices.camelmicroservicea.routes.patterns;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EipPatternsRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// Pipeline
		
		// Content Based - choice()
		
		// Multicast
		
		
		//from("timer:multicast?period=10000")
		//.multicast()
		//.to("log:something1", "log:something2", "log:something3");
	
		
		// Multiples files to many routes
		
		//from("file:files/csv")
				//.multicast()
				//.to("file:files/output1","file:files/output2","file:files/output3");
		
		
		// Split file to many routes
		
		from("file:files/csv")
		.unmarshal().csv()
		.split(body())
		.to("activemq:split-queue");
		
		
		
		
	}

}
