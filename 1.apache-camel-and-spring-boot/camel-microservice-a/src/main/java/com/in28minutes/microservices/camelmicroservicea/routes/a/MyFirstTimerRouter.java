package com.in28minutes.microservices.camelmicroservicea.routes.a;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyFirstTimerRouter extends RouteBuilder {

   

	@Autowired
    private GetCurrentTimeBean getCurrentTimeBean;

    @Autowired
    private SimpleLogginProcessingComponent simpleLogginProcessingComponent;

    @Override
    public void configure() throws Exception {
        // queue | timer
        // transformation
        // database | log
        // Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]
        from("timer:first-timer") // queue | 1. null
                .log("${body}") // 2. null
                .transform().constant("My constant Message") // 3. My constant Message
                .log("${body}") // 4. My constant Message
                // .transform().constant("Time is now" + LocalDateTime.now())

                // Processing =  receive | I want some operation or change | on the body of the message itself = from | That is called a processing
                // Transformation = When a thing transform to other thing

                .bean(getCurrentTimeBean, "getCurrentTime") // .bean(getCurrentTimeBean, "getCurrentTime") // can add more methods
                .log("${body}") // Time now is2021-07-22T20:18:42.213169400]
                .bean(simpleLogginProcessingComponent)
                .log("${body}")
                .process(new SimpleLoggingProcessor())
                .to("log:first-timer"); // database

    }
}


@Component
class GetCurrentTimeBean{
    public String getCurrentTime(){
        return "Time now is" + LocalDateTime.now();
    }
}


@Component
class SimpleLogginProcessingComponent{
    private Logger logger = LoggerFactory.getLogger(SimpleLogginProcessingComponent.class);
    public void process( String message){
        logger.info("SimpleLogginProcessingComponent {}", message);
    }
}


class SimpleLoggingProcessor implements Processor {

    private Logger logger = LoggerFactory.getLogger(SimpleLogginProcessingComponent.class);
	
	@Override
	public void process(Exchange exchange) throws Exception {
        logger.info("SimpleLogginProcessing {}", exchange.getMessage().getBody());

	}

}
