package com.q9090960bnb3.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class QpsTestController {
 
    @GetMapping("/test")
    public String qpsTest(){
        return "ok";
    }

    @GetMapping("/test2")
    public String qpsTest2() {
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "ok";
    }
    
}
