package cades.icar.rest.json;

import java.io.PrintStream;

import cades.icar.rest.json.EventHandler;
import cades.icar.rest.json.JsonParser;

/**
 * Pretty print handler for JSON push parser.
 * @author Pierre-Yves Gibello
 */
public class PrettyPrintHandler implements EventHandler {

	PrintStream out;
	int level = 0;
	boolean firstKey = true;
	boolean firstVal = true;

	public PrettyPrintHandler(PrintStream out) {
		this.out = out;
	}

	@Override
	public void startParsing(JsonParser parser) throws Exception {
	}

	@Override
	public void endParsing() throws Exception {
		if(this.out != null) this.out.flush();
	}

	@Override
	public void startObject() throws Exception {
		if(this.level > 0) print(0, (firstVal ? "" : ",\n")); //+ "\n");
		print(this.level++, "{\n");
		this.firstKey = true;
		this.firstVal = false;
	}

	@Override
	public void endObject() throws Exception {
		print(0, "\n");
		print(--this.level, "}" + (this.level <= 0 ? "\n" : ""));
		this.firstVal = false;
	}

	@Override
	public void key(String key) throws Exception {
		if(! this.firstKey) {
			print(0, ",\n");
		}
		print(this.level, "\"" + key + "\": ");
		this.firstKey = false;
		this.firstVal = true;
	}

	@Override
	public void simpleValue(String value) throws Exception {
		if(this.firstVal) print(0, "\"" + value + "\"");
		else {
			print(0, ",\n");
			print(this.level, "\"" + value + "\"");
		}
	}

	@Override
	public void startArray() throws Exception {
		print(0, "\n");
		print(++this.level, "[\n");
		++this.level;
		this.firstVal = true;
	}

	@Override
	public void endArray() throws Exception {
		print(0, "\n");
		this.level--;
		print(this.level--, "]");
	}
	
	private void print(int level, String str) {
		if(this.out != null) {
			for(int i=0; i < level; i++) this.out.print("  ");
			this.out.print(str);
		}
	}
}
