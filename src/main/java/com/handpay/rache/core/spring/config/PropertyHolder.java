package com.handpay.rache.core.spring.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyHolder extends PropertyPlaceholderConfigurer {
	private static Properties properties;
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
			throws BeansException {

		super.processProperties(beanFactory, props);
		
		properties = props;
	}

	public static Properties getProperties() {
		return properties;
	}
}
