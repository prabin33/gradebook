package com.dgb;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware{
 
    private  ApplicationContext context;
     
    public ApplicationContextProvider() {
		System.out.println("application context provider");
	}
    public  ApplicationContext getApplicationContext() {
        return context;
    }
     
    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        context = ac;
    }

}
