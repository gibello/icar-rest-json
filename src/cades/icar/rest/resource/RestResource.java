package cades.icar.rest.resource;

import java.io.IOException;

import javax.servlet.ServletException;

import cades.icar.rest.servlet.RequestContext;

/**
 * Abstraction (and default implementation) of a REST resource handler.
 * Handles PUT/GET/POST/DELETE HTTP methods, generally mapped to CRUD operations.
 * Resources are referenced by URIs that look like the following:
 * /category[/resourcename[/operation]] [?queryString]
 * Example: /library/books/add?{title:"Alice in wonderland"} may reference the insertion
 * of a book in the library (alternative: a HTTP PUT method on a "/library/book" resource).
 * @author Pierre-Yves Gibello
 */
public class RestResource {

	String name_;
	boolean default_;
	String contentType_;
	String defaultGetOp_;
	String defaultPostOp_;
	String defaultPutOp_;
	String defaultDeleteOp_;

	/**
	 * Create REST resource handler. The name is important for resource registration
	 * (Eg. /library/books should be mapped by a resource whose name is "books").
	 * @param name The resource name.
	 */
	public RestResource(String name) {
		name_ = name;
	}

	/**
	 * Handle HTTP GET method (generally mapped to READ in CRUD).
	 * @param operation The operation requested on this resource (may be null).
	 * @param ctx The HTTP request context
	 * @return The invocation result (eg. JSON data or HTML text).
	 * @throws ServletException
	 * @throws IOException
	 */
	public String onGet(String operation, RequestContext ctx) throws ServletException, IOException {
		return "{ error: \"onGet() not implemented\" }";
	}
	
	/**
	 * Handle HTTP POST method (generally mapped to UPDATE in CRUD).
	 * @param operation The operation requested on this resource (may be null).
	 * @param ctx The HTTP request context
	 * @return The invocation result (eg. JSON data or HTML text).
	 * @throws ServletException
	 * @throws IOException
	 */
	public String onPost(String operation, RequestContext ctx) throws ServletException, IOException {
		return "{ error: \"onPost() not implemented\" }";
	}
	
	/**
	 * Handle HTTP PUT method (generally mapped to CREATE in CRUD).
	 * @param operation The operation requested on this resource (may be null).
	 * @param ctx The HTTP request context
	 * @return The invocation result (eg. JSON data or HTML text).
	 * @throws ServletException
	 * @throws IOException
	 */
	public String onPut(String operation, RequestContext ctx) throws ServletException, IOException {
		return "{ error: \"onPut() not implemented\" }";
	}
	
	/**
	 * Handle HTTP DELETE method (generally mapped to DELETE in CRUD).
	 * @param operation The operation requested on this resource (may be null).
	 * @param ctx The HTTP request context
	 * @return The invocation result (eg. JSON data or HTML text).
	 * @throws ServletException
	 * @throws IOException
	 */
	public String onDelete(String operation, RequestContext ctx) throws ServletException, IOException {
		return "{ error: \"onDelete() not implemented\" }";
	}

	/**
	 * Tell whether this resource is the default handler for this resource name
	 * (to be invoked if no operation is specified).
	 * @return true if default handler, false otherwise.
	 */
	public final boolean isDefault() {
		return default_;
	}

	/**
	 * Make this resource the default handler for this resource name
	 * (to be invoked if no operation is specified).
	 * @param dfl true to make this resource the default one, false otherwise.
	 */
	public final void setDefault(boolean dfl) {
		default_ = dfl;
	}

	public final String getContentType() {
		return (contentType_ == null ? "text/plain" : contentType_);
	}

	public final void setContentType(String contentType) {
		contentType_ = contentType;
	}

	/**
	 * Retrieve this resource's name.
	 * @return The resource name.
	 */
	public final String getName() { return name_; }

	public final String getDefaultGetOperation() { return defaultGetOp_; }
	public final void setDefaultGetOperation(String operation) {
		defaultGetOp_ = operation;
	}
	
	public final String getDefaultPostOperation() { return defaultPostOp_; }
	public final void setDefaultPostOperation(String operation) {
		defaultPostOp_ = operation;
	}
	
	public final String getDefaultPutOperation() { return defaultPutOp_; }
	public final void setDefaultPutOperation(String operation) {
		defaultPutOp_ = operation;
	}
	
	public final String getDefaultDeleteOperation() { return defaultDeleteOp_; }
	public final void setDefaultDeleteOperation(String operation) {
		defaultDeleteOp_ = operation;
	}
	
	public final void setDefaultOperation(String operation) {
		defaultGetOp_ = operation;
		defaultPostOp_ = operation;
		defaultPutOp_ = operation;
		defaultDeleteOp_ = operation;
	}
}
