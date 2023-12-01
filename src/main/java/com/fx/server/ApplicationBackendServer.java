package com.fx.server;

import com.fx.server.grpc.GrpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

//@PropertySource(value = {"file:///${HOMEDIR}/application.server.properties"})
@ImportResource("classpath:META-INF\\spring\\server\\spring-context.xml")
@PropertySource("classpath:application.server.properties")
@PropertySource({
        "classpath:environment-independent.properties"
})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, WebMvcAutoConfiguration.class})
public class ApplicationBackendServer implements CommandLineRunner {

    @Autowired
    GrpcServer server;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBackendServer.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        server.startServer();
        System.out.println("GRPC server started!!!");
    }

}
