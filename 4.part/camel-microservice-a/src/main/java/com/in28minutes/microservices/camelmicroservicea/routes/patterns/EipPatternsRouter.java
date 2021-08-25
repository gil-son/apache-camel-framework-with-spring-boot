package com.in28minutes.microservices.camelmicroservicea.routes.patterns;

import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EipPatternsRouter extends RouteBuilder {

	
	private final SplitterComponent splitterComponent;
	
	EipPatternsRouter(SplitterComponent splitterComponent){
		this.splitterComponent = splitterComponent;
	}
	
	
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
		
		// from("file:files/csv")
		// .unmarshal().csv()
		// .split(body())
		// .to("activemq:split-queue");
		
		// Message, Message2, Message3
		
		from("file:files/csv")
		.convertBodyTo(String.class)
		// .split(body(),",")
		.split(method(splitterComponent))
		.to("activemq:split-queue");
		
	}

}



@Component
class SplitterComponent{
	public List<String> sliptInput(String body){
		return List.of("ABC","DEF","GHI");
	}
	
}
