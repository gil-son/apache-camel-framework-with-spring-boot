package com.in28minutes.microservices.camelmicroservicea.routes.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.in28minutes.microservices.camelmicroservicea.CurrencyExchange;

@Component
public class EipPatternsRouter extends RouteBuilder {

	@Autowired
	private SplitterComponent splitterComponent;
	
	@Autowired
	private DynamicRouterBean dynamicRouterBean;
	
	
//	EipPatternsRouter(SplitterComponent splitterComponent){
//		this.splitterComponent = splitterComponent;
//	}
//	
//	
//	EipPatternsRouter(DynamicRouterBean dynamicRouterBean){
//		this.dynamicRouterBean = dynamicRouterBean;
//	}
	
	
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
		
		//from("file:files/csv")
		//.convertBodyTo(String.class)
		//	// .split(body(),",")
		//.split(method(splitterComponent))
		//.to("activemq:split-queue");
		
		
		//Aggregate
		//Messages => Aggregate => Endpoint
		//to, 3 messages
		from("file:files/aggregate-json")
		.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
		.aggregate(simple("${body.getTo}"), new ArrayListAggregationStrategy())
		.completionSize(3)
		//.completionTimeout(HIGHEST)
		.to("log:aggregatte-json");
		
		String routingSlip = "direct:endpoint1,direct:endpoint2";
		//String routingSlip = "direct:endpoint1,direct:endpoint2,direct:endpoint3";
		
		//		from("timer:routingSlip?period=10000")
		//		.transform().constant("My Message is Hardcoded")
		//		.routingSlip(simple(routingSlip));
		
		
		// Dynamic Pattern Routing
		
		// Steps: Step 1, Step 2, Step 3
		
		from("timer:dynamicRouting?period=10000")
		.transform().constant("My Message is Hardcoded")
		.dynamicRouter(method(dynamicRouterBean));
		
		// Endpoints: Endpoint1, Endpoint2 and Endpoint3
		
		
		
		from("direct:endpoint1")
		.to("log:direct:endpoint1");		
		
		from("direct:endpoint2")
		.to("log:direct:endpoint2");
		
		from("direct:endpoint3")
		.to("log:direct:endpoint3");
	}

}



@Component
class SplitterComponent{
	public List<String> sliptInput(String body){
		return List.of("ABC","DEF","GHI");
	}
	
}




@Component
class DynamicRouterBean{
	
	Logger logger = LoggerFactory.getLogger(DynamicRouterBean.class);
	
	int invocations;
	
	public String decideTheNextEndpoint(
				@ExchangeProperties Map<String, String> properties,
				@Headers Map<String, String> headers,
				@Body String body
			) {
		
			logger.info("{} {} {}", properties, headers, body);
			
			invocations++;
			
			
			if(invocations%3==0)
				return "direct:endpoint1";
			
			if(invocations%3==1)
				return "direct:endpoint2,direct:endpoint3";
			
			return null;
			
	}
	
}



