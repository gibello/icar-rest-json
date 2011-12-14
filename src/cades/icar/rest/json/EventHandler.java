package cades.icar.rest.json;

/**
 * Event handler interface for JSON push parser.
 * @author Pierre-Yves Gibello
 */
public interface EventHandler {
	
	public void startParsing(JsonParser parser) throws Exception;
	public void endParsing() throws Exception;
	
	public void startObject() throws Exception;
	public void endObject() throws Exception;
	public void startArray() throws Exception;
	public void endArray() throws Exception;

	public void key(String key) throws Exception;
	public void simpleValue(String val) throws Exception;
}
