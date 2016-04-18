package org.logpresso.sentry.example;

import java.util.Arrays;
import java.util.Collection;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.logpresso.sentry.SentryCommandHandler;
import org.logpresso.sentry.SentryMethod;

@Component(name = "sentry-hello-plugin")
@Provides
public class HelloCommandHandler implements SentryCommandHandler {

	@Override
	public Collection<String> getFeatures() {
		return Arrays.asList("hello");
	}

	@SentryMethod
	public String hello(String name) {
		return "hello, " + name;
	}

}
