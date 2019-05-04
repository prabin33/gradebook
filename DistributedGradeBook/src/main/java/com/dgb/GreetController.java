package com.dgb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetController {

	@Autowired
	ApplicationContextProvider context;
	
	public GreetController() {
		System.out.println("greet controller "+context);
	}
	@RequestMapping("/greet")
	public String greet()
	{
		System.out.println("greet controller greet() "+context);
		System.out.println();
//		System.out.println(DistributedGradeBookApplication.getPort());
		if(Integer.parseInt(context.getApplicationContext().getEnvironment().getProperty("server.port"))== 8090)
			return "Greetings";
		return "Unauthorized apart from post 8090";
	}
	@RequestMapping("/wel")
	public String welcome()
	{
		return "Welocme";
	}
}
