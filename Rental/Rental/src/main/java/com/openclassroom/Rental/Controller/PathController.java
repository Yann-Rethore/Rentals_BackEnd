package com.openclassroom.Rental.Controller;

import jakarta.servlet.ServletContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class PathController {

    private ServletContext servletContext;

    @GetMapping("/path-root")
    public String getRootPath() {
        return servletContext.getRealPath("/");
    }
}
