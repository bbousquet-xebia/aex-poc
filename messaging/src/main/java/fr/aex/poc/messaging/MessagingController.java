package fr.aex.poc.messaging;

import io.cloudevents.CloudEvent;
import io.cloudevents.json.Json;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
public class MessagingController {
    @RequestMapping("/")
    public String index(HttpServletRequest request) throws IOException {

        // Convert the request inputStream to string
        String str_request = IOUtils.toString(request.getInputStream(), "UTF-8");

        CloudEvent<Event> cloudEvent = Json.fromInputStream(new ByteArrayInputStream(str_request.getBytes()));

        System.out.println(cloudEvent);

        return "Greetings from Spring Boot!";
    }
}
