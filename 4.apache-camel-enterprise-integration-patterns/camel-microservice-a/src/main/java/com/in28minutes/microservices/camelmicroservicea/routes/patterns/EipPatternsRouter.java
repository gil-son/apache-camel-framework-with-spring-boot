package com.in28minutes.microservices.camelmicroservicea.routes.patterns;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import com.in28minutes.microservices.camelmicroservicea.CurrencyExchange;

@Component
public class EipPatternsRouter extends RouteBuilder {

	public class ArrayListAggregationStrategy implements AggregationStrategy {
		// 1,2,3
		// old , new
		// null, 1 => [[1]]
		// [[1]], 2 => [[1],[2]]
		// [[1],[2]] => [[1],[2],[3]]
		
		
		@Override
		public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
			Object newBody = newExchange.getIn().getBody();
			ArrayList<Object> list = null;
			if(oldExchange == null) {
				list = new ArrayList<Object>();
				list.add(newBody);
				newExchange.getIn().setBody(list);
				return newExchange;
			}else {
				list = oldExchange.getIn().getBody(ArrayList.class);
				list.add(newBody);
				return oldExchange;
			}
		}
	}
	
	
	
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
		
		
	}

}



@Component
class SplitterComponent{
	public List<String> sliptInput(String body){
		return List.of("ABC","DEF","GHI");
	}
	
}

