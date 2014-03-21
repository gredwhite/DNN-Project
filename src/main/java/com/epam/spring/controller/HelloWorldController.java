package com.epam.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {

	@RequestMapping("/hello")
    @ResponseBody
	public String hello(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		return "helloworld";
	}

}
