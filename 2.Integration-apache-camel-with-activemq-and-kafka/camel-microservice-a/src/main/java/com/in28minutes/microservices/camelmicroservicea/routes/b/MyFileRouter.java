package com.in28minutes.microservices.camelmicroservicea.routes.b;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyFileRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("file:files/input")
        .routeId("Files-Input-Route")
        .transform().body(String.class) // convert to String the log part
        .choice()
        	.when(simple("${file:ext} == 'xml'")) // ${file:ext} ends with 'xml
        		.log("XML FILE")
        	.when(simple("${body} contains 'USD'"))
        		.log("Not an XML FILE BUT contains USD")
        	.otherwise()
        		.log("Not an XML FILE")
        .end()
        //.log("${body}")
        
        .to("direct:log-file-values")
        .to("file:files/output");
        
        from("direct:log-file-values")

        .log("${messageHistory} ${headers.CamelFileAbsolute}")
        .log("${messageHistory} ${file:absolute.path}") // https://camel.apache.org/components/latest/languages/file-language.html
        .log("${messageHistory} ${file:onlyname}")
        .log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
        .log("${file:onlyname.noext} ${file:path} ${file:absolute}")
        .log("${routeId} ${camelId} ${body}");
    }
}
