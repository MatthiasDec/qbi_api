package com.qbi.config;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalConfiguration {

	@PostConstruct
	public void init() {

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		System.out.println("Serveur Time: " + new Date().toString());
	}
}
