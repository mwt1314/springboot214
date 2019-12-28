package cn.matio.api.swagger2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {

    @GetMapping("/dev/swagger")
    @RequiresPermissions("/dev/swagger")
    public String index(){
        return "redirect:/swagger-ui.html";
    }

}