package com.jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    //    Logger logger = LoggerFactory.getLogger(getClass());
    @RequestMapping("/home")
    public String home() {
        log.info("HomeController.home");
        return "home";
    }
}
