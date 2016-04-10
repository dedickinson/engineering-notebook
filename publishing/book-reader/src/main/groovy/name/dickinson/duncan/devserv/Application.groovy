package name.dickinson.duncan.devserv

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@ComponentScan
@Controller
@EnableAutoConfiguration
public class Application {

    /*
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String index() {
        "Hi!"
    }*/
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
