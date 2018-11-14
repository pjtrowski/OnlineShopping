package com.hoyrak.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringWebAppInitializer implements WebApplicationInitializer 
	{
		@Override
		public void onStartup(ServletContext servletContext) 
		{
			//servlet registration and context configuration
			AnnotationConfigWebApplicationContext appContext=new AnnotationConfigWebApplicationContext();
			appContext.register(ApplicationContextConfig.class);
			
			ServletRegistration.Dynamic dispatcher=servletContext.addServlet("SpringDispatcher",new DispatcherServlet(appContext));
			dispatcher.setLoadOnStartup(1);
			dispatcher.addMapping("/");
			
			ContextLoaderListener contextLoadListener=new ContextLoaderListener(appContext);
			servletContext.addListener(contextLoadListener);
			
			FilterRegistration.Dynamic fr=servletContext.addFilter("encodigFilter", CharacterEncodingFilter.class);
			fr.setInitParameter("encodifn", "UTF-8");
			fr.setInitParameter("forceEncoding","true");
			fr.addMappingForUrlPatterns(null, true, "/");
	
		}
	}
