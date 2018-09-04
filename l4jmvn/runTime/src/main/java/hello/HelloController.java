package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
    // private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/")
    public String index() {
        // logger.info("In the index");
        return "Greetings from Spring Boot!";
    }

}
