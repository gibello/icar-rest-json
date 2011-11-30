package cades.icar.rest.sample;

import cades.icar.rest.resource.ResourceRegistry;
import cades.icar.rest.servlet.SimpleRestServlet;


/**
 * Servlet implementation class DemoRestServlet
 */
public class DemoRestServlet extends SimpleRestServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void initRestResources(ResourceRegistry registry) {
		// Provide resource handler for "/demo/hello"
		HelloWorldResource hello = new HelloWorldResource("hello");
		hello.setDefault(true); // Flag as default resource for "/demo"
		registry.registerResource("demo", hello);
		
		// Provide resource handler for "/demo/displayName"
		registry.registerResource("demo", new DisplayNameResource("greetings"));
	}
}
