package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@ComponentScan
public class WebMvcConfig implements WebMvcConfigurer {
	
	WebMvcConfig(){
		System.out.println("Web Config Upload");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry reg) {
		reg.addResourceHandler("/resources/static/**").addResourceLocations("/resourses");
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addViewController("/").setViewName("forward:/index.html");
	}

}
