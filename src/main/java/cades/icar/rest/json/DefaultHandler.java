package cades.icar.rest.json;

/**
 * Default handler for JSON push parser. To be overridden.
 * @author Pierre-Yves Gibello
 */
public class DefaultHandler implements EventHandler {

	@Override
	public void startParsing(JsonParser parser) throws Exception { }

	@Override
	public void endParsing() throws Exception { }

	@Override
	public void startObject() throws Exception { }

	@Override
	public void endObject() throws Exception { }

	@Override
	public void key(String key) throws Exception { }

	@Override
	public void simpleValue(String val) throws Exception { }

	@Override
	public void startArray() throws Exception { }

	@Override
	public void endArray() throws Exception { }
}
