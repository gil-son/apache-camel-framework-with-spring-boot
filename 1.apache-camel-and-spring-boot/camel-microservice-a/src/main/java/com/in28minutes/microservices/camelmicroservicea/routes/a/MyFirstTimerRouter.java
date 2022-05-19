package com.in28minutes.microservices.camelmicroservicea.routes.a;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
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

    @Autowired
     private Transform01 transform01;


    @Override
    public void configure() throws Exception {
        // QUEUE | timer

        // database | log
        // Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]
        from("timer:first-timer") // queue || null

                // PROCESS AND TRANSFORMATION
                .transform().constant("My Constant Message")
                .process(transform01)
                .log("the first route") // database
                .log("${body}") // body =  null
                .transform().constant("My constant Message") // 3. My constant Message
                .log("${body}") // body = My constant Message
                // .transform().constant("Time is now" + LocalDateTime.now())

                // Processing =  receive | I want some operation or change | on the body of the message itself = from | That is called a processing
                // Transformation = When a thing transform to other thing
//                .bean("getCurrentTimeBean")
                //.bean(getCurrentTimeBean) // .bean(getCurrentTimeBean, "getCurrentTime") // can add more methods
                //.log("${body}") // Time now is2021-07-22T20:18:42.213169400]
                .bean(simpleLogginProcessingComponent) // did not replace, but concatenated
                .log("${body}")
//                .process(new SimpleLoggingProcessor())
                .to("log:first-timer"); // database

    }
}

@Component
class Transform01 implements Processor{

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getMessage().setBody("Modify by Transform01:" + LocalDateTime.now());
    }
}

@Component
class GetCurrentTimeBean{
    public String getCurrentTime(){
        return "Time now is: " + LocalDateTime.now();
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
