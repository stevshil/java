package com.tpsservices.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController

public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/*
	// The original request
	@RequestMapping(value="/") 
	public String hello() {
		return "Hello World";
	}
	*/
	// Our / is now served from the resources/static directory

	@RequestMapping(value="/on")
	public String hello() {
		return "Hello World";
	}

	@RequestMapping(value="/off")
	public String goodbye() {
		return "Goodbye";
	}

	// @GetMapping is an annotation and needs to be imported like all other @ annotations, and so is RequestMethod
	@GetMapping(value="/hello")
	@ResponseBody
	public String sayname(@RequestParam String name) {
		return "Hello "+name;
	}
	// The above is called with http://yourhost/hello?name=Steve

}