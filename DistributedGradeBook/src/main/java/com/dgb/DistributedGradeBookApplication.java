package com.dgb;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class DistributedGradeBookApplication{

	private static Integer port;
	private static ArrayList<String> knownNames = null;

	public static String secondary_host;	
	
	private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(DistributedGradeBookApplication.class);
	
	public static void main(String[] args) throws Exception
	{
		SpringApplication application = new SpringApplication(DistributedGradeBookApplication.class);

        Properties properties = new Properties();
        properties.put("server.port", 8080);
        application.setDefaultProperties(properties);
        ApplicationContext context = application.run(args);
        
		for (String arg : args){
			
			String[] parts = arg.split("=");
			if (parts.length == 2){
				if (parts[0].equals("secondary_host")){
					DistributedGradeBookApplication.secondary_host = parts[1];
				}
			}
		}
		if (DistributedGradeBookApplication.secondary_host != null){
			System.out.println("Detected secondary host as " + DistributedGradeBookApplication.secondary_host);
			
			SpringApplication secapplication = new SpringApplication(DistributedGradeBookApplication.class);

	        Properties secproperties = new Properties();
	        secproperties.put("server.port", 8090);
	        secapplication.setDefaultProperties(secproperties);

	        ConfigurableApplicationContext seccontext = secapplication.run(args);
	        
	       
			System.out.println("ssec server port "+Integer.parseInt(seccontext.getEnvironment().getProperty("local.server.port")));
			
		} else{
			throw new Exception("You must specify secondary_host as an argument. Example: secondary_host=\"127.0.0.1:8090\"");
		}
		  System.out.println("primary server port "+Integer.parseInt(context.getEnvironment().getProperty("local.server.port")));		
	}

	
}
