package com.fx.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"file:///${HOMEDIR}/application.client.properties"})
@ImportResource("classpath:META-INF\\spring\\client\\spring-context.xml")
@PropertySource({
        "classpath:environment-independent.properties"
})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ApplicationGatewayServer {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationGatewayServer.class, args);
        System.out.println("Web server started!!!");
    }

}
