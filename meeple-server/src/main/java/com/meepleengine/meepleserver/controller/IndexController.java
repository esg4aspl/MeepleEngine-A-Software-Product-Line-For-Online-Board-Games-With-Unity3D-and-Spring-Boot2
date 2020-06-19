package com.meepleengine.meepleserver.controller;

import com.meepleengine.meepleserver.config.SwaggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class IndexController {

    private final SwaggerConfig swaggerConfig;

    @Autowired
    public IndexController(SwaggerConfig swaggerConfig) {
        this.swaggerConfig = swaggerConfig;
    }

    @GetMapping
    public RedirectView redirectToSwagger() {
        return new RedirectView(String.format("http://%s%s/swagger-ui.html", swaggerConfig.getHost(), swaggerConfig.getPath()));
    }

}
