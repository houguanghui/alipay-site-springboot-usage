package com.alipay.alipaydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class AlipayDemoApplication {

    @GetMapping("/")
    public String index() {
        return "index";

    }



    public static void main(String[] args) {
        SpringApplication.run(AlipayDemoApplication.class, args);
    }

}
