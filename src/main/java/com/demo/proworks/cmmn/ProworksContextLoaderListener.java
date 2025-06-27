package com.demo.proworks.cmmn;

import javax.servlet.ServletContextEvent;

import com.inswave.elfw.core.ElContextLoaderListener;
import com.inswave.elfw.log.AppLog;

public class ProworksContextLoaderListener extends ElContextLoaderListener {
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
	    super.contextInitialized(event);
	    
	    AppLog.debug("ProworksContextLoaderListener - contextInitialized ...");    	   
		
	}
	 
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
		
		AppLog.debug("ProworksContextLoaderListener - contextDestroyed ...");    	   
				
	}	
}
