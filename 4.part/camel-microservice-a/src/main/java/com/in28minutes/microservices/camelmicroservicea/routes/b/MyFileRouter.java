package com.in28minutes.microservices.camelmicroservicea.routes.b;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MyFileRouter extends RouteBuilder {

	@Autowired
	private DeciderBean deciderBean;
	
    @Override
    public void configure() throws Exception {
    	// Pipiline
    	
        from("file:files/input")
        // .pipiline()
        .routeId("Files-Input-Route")
        .transform().body(String.class) // convert to String the log part
        .choice()
        	.when(simple("${file:ext} == 'xml'")) // ${file:ext} ends with 'xml
        		.log("XML FILE")
        	// .when(simple("${body} contains 'USD'"))
        		.when(method(deciderBean)) // true/false
        		.log("Not an XML FILE BUT contains USD") // true
        	.otherwise()
        		.log("Not an XML FILE") // else
        .end()
        //.log("${body}")
        
        .to("direct:log-file-values")
        .to("file:files/output");
        
        from("direct:log-file-values")

        //.log("${messageHistory} ${headers.CamelFileAbsolute}")
        //.log("${messageHistory} ${file:absolute.path}") // https://camel.apache.org/components/latest/languages/file-language.html
        //.log("${messageHistory} ${file:onlyname}")
        //.log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
        //.log("${file:onlyname.noext} ${file:path} ${file:absolute}")
        .log("${routeId} ${camelId} ${body}");
    }
}

@Component
class DeciderBean{
	
	Logger logger = LoggerFactory.getLogger(DeciderBean.class);
	
	public boolean isThisConditionMet(@Body String body,
			@Headers Map<String, String> headers,
			@ExchangeProperties Map<String, String> exchangeProperties) {
		logger.info("DeciderBean {} {} {}", body, headers, exchangeProperties);
				
		return true;
	}
}
