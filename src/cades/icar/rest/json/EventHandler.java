package cades.icar.rest.json;

/**
 * Event handler interface for JSON push parser.
 * @author Pierre-Yves Gibello
 */
public interface EventHandler {
	
	public void startParsing(JsonParser parser);
	public void endParsing();
	
	public void startObject();
	public void endObject();
	public void startArray();
	public void endArray();

	public void key(String key);
	public void simpleValue(String val);
}
