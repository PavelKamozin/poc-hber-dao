package softserve.hibernate.com.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("SwaggerController")
@Api(value = "SwaggerController", description = "Exposes APIs to work with Swagger resource.")
public class SwaggerController {

    @RequestMapping("/")
    public String getSwagger() {
        return "redirect:/swagger-ui.html";
    }

}
