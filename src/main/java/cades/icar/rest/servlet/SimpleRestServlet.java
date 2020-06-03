package cades.icar.rest.servlet;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cades.icar.rest.resource.ResourceRegistry;
import cades.icar.rest.resource.RestResource;

/**
 * Servlet implementation class SimpleRestServlet
 * @author Pierre-Yves Gibello
 */
public abstract class SimpleRestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	ResourceRegistry registry_;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		registry_ = ResourceRegistry.getInstance();
		initRestResources(registry_);
	}
	
	public abstract void initRestResources(ResourceRegistry registry);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String parts[] = parseURI(request.getRequestURI());
    	RestResource resource = registry_.getResource(parts[0], parts[1]);
    	if(resource != null) {
    		response.setContentType(resource.getContentType());
    		String operation = (parts[2] != null ? parts[2] : resource.getDefaultGetOperation());
    		response.getWriter().print(
    				resource.onGet(operation, new RequestContext(request, response)));
    	} else {
    		response.sendError(404, "No resource found for " + parts[0] + "/" + parts[1]);
    	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String parts[] = parseURI(request.getRequestURI());
    	RestResource resource = registry_.getResource(parts[0], parts[1]);
    	if(resource != null) {
    		response.setContentType(resource.getContentType());
    		String operation = (parts[2] != null ? parts[2] : resource.getDefaultPostOperation());
    		response.getWriter().print(
    				resource.onPost(operation, new RequestContext(request, response)));
    	} else {
    		response.sendError(404, "No resource found for " + parts[0] + "/" + parts[1]);
    	}
	}

    /**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String parts[] = parseURI(request.getRequestURI());
    	RestResource resource = registry_.getResource(parts[0], parts[1]);
    	if(resource != null) {
    		response.setContentType(resource.getContentType());
    		String operation = (parts[2] != null ? parts[2] : resource.getDefaultDeleteOperation());
    		response.getWriter().print(
    				resource.onDelete(operation, new RequestContext(request, response)));
    	} else {
    		response.sendError(404, "No resource found for " + parts[0] + "/" + parts[1]);
    	}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String parts[] = parseURI(request.getRequestURI());
    	RestResource resource = registry_.getResource(parts[0], parts[1]);
    	if(resource != null) {
    		response.setContentType(resource.getContentType());
    		String operation = (parts[2] != null ? parts[2] : resource.getDefaultPutOperation());
    		response.getWriter().print(
    				resource.onPut(operation, new RequestContext(request, response)));
    	} else {
    		response.sendError(404, "No resource found for " + parts[0] + "/" + parts[1]);
    	}
	}

	/**
	 * Parse an URI, expected of the following form:
	 * /webappName [/category[/resourcename[/operation]]] [?queryString]
	 * Eg. /calcservice/simple/calculator/add?[1,3]
	 * @param uri The URI to parse.
	 * @return A table of 3 strings (each one possibly null): category, resource name, operation.
	 */
	private String[] parseURI(String uri) {
		int pos = uri.indexOf("?");
		if(pos > 0) uri = uri.substring(0, pos-1);

		String parts[] = new String[3];
		parts[0] = parts[1] = parts[2] = null;

		StringTokenizer st = new StringTokenizer(uri, "/");
		if(st.hasMoreTokens()) st.nextToken(); // skip webapp name
    	if(st.hasMoreTokens()) {
    		parts[0] = st.nextToken();
    	}
    	if(st.hasMoreTokens()) {
    		parts[1] = st.nextToken();
    	}
    	if(st.hasMoreTokens()) {
    		parts[2] = st.nextToken();
    	}
    	
    	return parts;
	}
}
