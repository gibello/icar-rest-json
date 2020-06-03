package cades.icar.rest.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Context for HTTP servlet request
 * @author Pierre-Yves Gibello
 */
public class RequestContext {

	HttpServletRequest req;
	HttpServletResponse rsp;
	String queryString;

	public RequestContext(HttpServletRequest req,
			HttpServletResponse rsp) {
		super();
		this.req = req;
		this.rsp = rsp;
		this.queryString = req.getQueryString();
	}

	public HttpServletRequest getServletRequest() {
		return req;
	}

	public HttpServletResponse getServletResponse() {
		return rsp;
	}
	
	public String getQueryString() {
		return queryString;
	}
}
