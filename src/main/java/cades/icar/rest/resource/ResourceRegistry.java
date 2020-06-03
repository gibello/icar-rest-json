package cades.icar.rest.resource;

import java.util.HashMap;

/**
 * Global registry for REST resource handlers.
 * Useful to register/unregister/retrieve resources, given their category + name.
 * @author Pierre-Yves Gibello
 */
public class ResourceRegistry {

	static ResourceRegistry theRegistry_;
	HashMap<String, HashMap<String, RestResource>> resourceMap_ = new HashMap<String, HashMap<String, RestResource>>();

	protected ResourceRegistry() { }
	
	/**
	 * Retrieve registry (singleton pattern)
	 * @return
	 */
	public static synchronized ResourceRegistry getInstance() {
		if(theRegistry_ == null) theRegistry_ = new ResourceRegistry();
		return theRegistry_;
	}
	
	/**
	 * Store resource in registry, given its category + name (dual key).
	 * Eg. "/category/resourcename" corresponds to registerResource("category", res)
	 * where res.getName() returns "resourcename".
	 * Key elements (both category and name) may be null or empty.
	 * @param category A category name
	 * @param resource A REST resource handler
	 */
	public synchronized void registerResource(String category, RestResource resource) {
		HashMap<String, RestResource> resources = resourceMap_.get(category);
		if(resources == null) {
			resources = new HashMap<String, RestResource>();
			resourceMap_.put(category, resources);
		}
		resources.put(resource.getName(), resource);
	}

	/**
	 * Remove a resource from registry, given its category and name (both may be null).
	 * @param category A category name
	 * @param resourceName A REST resource name
	 */
	public synchronized void unregisterResource(String category, String resourceName) {
		HashMap<String, RestResource> resources = resourceMap_.get(category);
		if(resources != null) {
			resources.remove(resourceName);
			if(resources.isEmpty()) resourceMap_.remove(category);
		}
	}
	
	/**
	 * Lookup a resource in registry, given its category and name (both may be null).
	 * @param category A category name
	 * @param resourceName A REST resource name
	 * @return The requested resource, null if not found.
	 */
	public RestResource getResource(String category, String resourceName) {
		HashMap<String, RestResource> resources = resourceMap_.get(category);
		RestResource ret = resources.get(resourceName);
		if(ret == null && (resourceName == null || resourceName.length() < 1)) {
			for(RestResource r : resources.values()) {
				if(r.isDefault()) {
					ret = r;
					break;
				}
			}
		}
		return ret;
	}
}
