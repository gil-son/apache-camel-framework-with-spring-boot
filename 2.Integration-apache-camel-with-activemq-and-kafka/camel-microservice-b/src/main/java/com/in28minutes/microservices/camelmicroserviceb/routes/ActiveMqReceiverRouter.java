package com.in28minutes.microservices.camelmicroserviceb.routes;

import java.math.BigDecimal;
import java.net.ConnectException;


import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.in28minutes.microservices.camelmicroserviceb.CurrencyExchange;


@Component
public class ActiveMqReceiverRouter extends RouteBuilder {

	@Autowired
	private MyCurrencyExchangeProcessor myCurrencyExchangeProcessor;
	
	@Autowired
	private MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;
	
	@Override
	public void configure() throws Exception {

		//JSON

		//1 - CurrencyExchange

//		 from("activemq:my-activemq-queue")
//				 .unmarshal()
//				 .json(JsonLibrary.Jackson, CurrencyExchange.class)
//		 .to("log:received-message-from-active-mq");


		//2 - CurrencyExchange - myCurrencyExchangeProcessor - myCurrencyExchangeTransformer

		// {"id": 1000,"from": "USD","to": "INR","conversionMultiple": 70}

//		from("activemq:my-activemq-queue")
//				.unmarshal()
//				.json(JsonLibrary.Jackson, CurrencyExchange.class)
//				.bean(myCurrencyExchangeProcessor)
//				.to("log:received-message-from-active-mq");

		// 3 - CurrencyExchange - myCurrencyExchangeProcessor -

		// {"id": 1000,"from": "USD","to": "INR","conversionMultiple": 70}

		from("activemq:my-activemq-queue")
			.unmarshal()
			.json(JsonLibrary.Jackson, CurrencyExchange.class)
			.bean(myCurrencyExchangeProcessor)
			.bean(myCurrencyExchangeTransformer)
		  	.to("log:received-message-from-active-mq");


		
		
		// XML
		/*
		 * <root>
   				<id>1000</id>
   				<from>USD</from>
   				<to>INR</to>
   				<conversionMultiple>70</conversionMultiple>
		   </root>
		 */
		
//		from("activemq:my-activemq-xml-queue")
//		.unmarshal()
//		.jacksonxml(CurrencyExchange.class)
//		.to("log:received-message-from-active-mq");


		// RECEIVED FROM QUEUE MESSAGE - CURRENCY EXCHANGE

//		from("activemq:my-activemq-queue")
//				.doTry()
//					.log("${body}")
				//.doCatch(ConnectException.class, ConnectException.class)
//					.to("direct:catch")
//				.doFinally()
//					.to("direct:finally")
//				.end()
//					.to("log:received-message-from-active-mq");




		// EXCEPTION


		// EXCEPTION

		from("direct:catch")
				.log(" Catch: Error queue connection")
				.end();


		from("direct:finally")
				.log("Finally: Error queue connection")
				.end();



	}
	
}

@Component
class MyCurrencyExchangeProcessor {
	
	Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeProcessor.class);
	
	public void processMessage(CurrencyExchange currencyExchange) {
		
		logger.info("Do some processing with currencyExchange.getConversionMultiple() value which is {}",
			currencyExchange.getConversionMultiple());
		
	}
}


@Component
class MyCurrencyExchangeTransformer{
	
	Logger logger = LoggerFactory.getLogger(MyCurrencyExchangeTransformer.class);
	
	public CurrencyExchange processMessage(CurrencyExchange currencyExchange) {

		// Is like a service
		currencyExchange.setConversionMultiple(
					currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN));
		
		logger.info("Do some processing with currencyExchange.getConversionMultiple() value... which is {}", currencyExchange.getConversionMultiple());
		
		return currencyExchange;
	}
}




