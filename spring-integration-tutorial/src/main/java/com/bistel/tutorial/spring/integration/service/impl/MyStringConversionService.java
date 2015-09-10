package com.bistel.tutorial.spring.integration.service.impl;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class MyStringConversionService implements com.bistel.tutorial.spring.integration.service.StringConversionService {

	@ServiceActivator
	@Override
	public String convertToUpperCase(String stringToConvert) {
		return "convert to  " + stringToConvert.toUpperCase();
	}

	@ServiceActivator
	@Override
	public String convertToLowerCase(String stringToConvert) {
		return "conver to " + stringToConvert.toLowerCase();
	}

}
