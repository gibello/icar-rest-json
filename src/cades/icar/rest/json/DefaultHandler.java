package cades.icar.rest.json;

/**
 * Default handler for JSON push parser. To be overridden.
 * @author Pierre-Yves Gibello
 */
public class DefaultHandler implements EventHandler {

	@Override
	public void startParsing(JsonParser parser) { }

	@Override
	public void endParsing() { }

	@Override
	public void startObject() { }

	@Override
	public void endObject() { }

	@Override
	public void key(String key) { }

	@Override
	public void simpleValue(String val) { }

	@Override
	public void startArray() { }

	@Override
	public void endArray() { }
}
