package cades.icar.rest.sample;

import cades.icar.rest.resource.RestResource;
import cades.icar.rest.servlet.RequestContext;


public class HelloWorldResource extends RestResource {

	public HelloWorldResource(String name) {
		super(name);
		// setContentType("application/json");
	}

	@Override
	public String onGet(String operation, RequestContext ctx) {
		return "{ greetings: \"Hello world\" }";
	}

	@Override
	public String onPost(String operation, RequestContext ctx) {
		return onGet(operation, ctx);
	}

}
