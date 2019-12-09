package com.junyoung.book.springboot.web;

import com.junyoung.book.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam(name = "name") String name, @RequestParam(name = "amount") int amount){

        return new HelloResponseDto(name,amount);
    }



}
