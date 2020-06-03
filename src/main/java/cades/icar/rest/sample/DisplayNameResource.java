package cades.icar.rest.sample;

import cades.icar.rest.resource.RestResource;
import cades.icar.rest.servlet.RequestContext;

public class DisplayNameResource extends RestResource {

	public DisplayNameResource(String name) {
		super(name);
		setContentType("text/html");
	}

	/**
	 * HTTP GET method
	 * URL may end with /<name> or provide HTTP parameter name=<name>
	 */
	@Override
	public String onGet(String operation, RequestContext ctx) {
		String name = ctx.getServletRequest().getParameter("name");
		if(name == null) name = operation;
		return (name != null && name.length()>=1 ? "Hello, " + name : "What's your name ?");
	}

	@Override
	public String onPost(String operation, RequestContext ctx) {
		return onGet(operation, ctx);
	}
}
