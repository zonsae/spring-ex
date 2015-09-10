/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bistel.tutorial.spring.integration;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

import com.bistel.tutorial.spring.integration.service.StringConversionService;


/**
 * Starts the Spring Context and will initialize the Spring Integration routes.
 *
 * @author Philip Ha
 * @since 1.0
 *
 */
public final class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);

	private Main() { }

	/**
	 * Load the Spring Integration Application Context
	 *
	 * @param args - command line arguments
	 */
	public static void main(final String... args) {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\n========================================================="
					  + "\n                                                         "
					  + "\n          Welcome to Spring Integration!                 "
					  + "\n                                                         "
					  + "\n    For more information please visit:                   "
					  + "\n    http://www.springsource.org/spring-integration       "
					  + "\n                                                         "
					  + "\n=========================================================" );
		}

		final AbstractApplicationContext context =
				new ClassPathXmlApplicationContext("classpath:META-INF/spring/integration/*-context.xml");

		context.registerShutdownHook();

		final Scanner scanner = new Scanner(System.in);

		final StringConversionService service = context.getBean(StringConversionService.class);
		final MessageChannel inputChanel = (MessageChannel)context.getBean("requestChannel", MessageChannel.class);
		final PollableChannel outputChanel = (PollableChannel)context.getBean("replyChannel", PollableChannel.class);
		
		final MessageChannel inputChanel2 = (MessageChannel)context.getBean("requestChannel2", MessageChannel.class);
		final PollableChannel outputChanel2 = (PollableChannel)context.getBean("replyChannel2", PollableChannel.class);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\n========================================================="
					  + "\n                                                         "
					  + "\n    Please press 'q + Enter' to quit the application.    "
					  + "\n                                                         "
					  + "\n=========================================================" );
		}

		System.out.print("Please enter a string and press <enter>: ");

		while (true) {

			final String input = scanner.nextLine();

			if("q".equals(input.trim())) {
				break;
			}

			try {

				//System.out.println("Converted to upper-case: " + service.convertToUpperCase(input));
								
				System.out.println("[ToUpper Service]");
				
				Message<?> inputMessage = MessageBuilder.withPayload(input).build();
				
				inputChanel.send(MessageBuilder.withPayload(input).build());
				System.out.println("service1 input : " + inputMessage.getPayload());
				System.out.println(inputMessage);
				
				Message<?> reply = outputChanel.receive();
				System.out.println("service1 output : " + reply.getPayload());
				System.out.println(reply);
				
				
				
				System.out.println();
				System.out.println("[ToLower Service]");
				
				inputChanel2.send(reply);
				System.out.println("service2 input : " + reply.getPayload());
				System.out.println(reply);
				
				Message<?> reply2 = outputChanel2.receive();
				System.out.println("service2 output : " + reply2.getPayload());
				System.out.println(reply2);
				
			} catch (Exception e) {
				LOGGER.error("An exception was caught: " + e);
			}

			System.out.print("Please enter a string and press <enter>:");

		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Exiting application...bye.");
		}

		System.exit(0);

	}
}
