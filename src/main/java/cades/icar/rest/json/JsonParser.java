package cades.icar.rest.json;

import java.io.IOException;
import java.io.InputStream;

import cades.icar.rest.json.EventHandler;

/**
 * JSON push parser (looks like XML sax parsing).
 * @author Pierre-Yves Gibello
 */
public class JsonParser {
	
	StringBuffer currentValue = new StringBuffer();
	int level = 0;
	int stack[] = new int[512];
	boolean record;
	StringBuilder recordBuffer = new StringBuilder();
	boolean quoted = false;
	
	private static final int OBJECT = 1;
	private static final int ARRAY = 2;
	private static final int VALUE = 4;
	private static final int KEY = 8;

	public void parse(InputStream in, EventHandler handler) throws Exception {
		int expect = OBJECT | ARRAY;
		int status = 0, mainIndex = 0;;
		boolean firstval = true;
		int c = 0;
		int prevc = 0;

		handler.startParsing(this);

		while((c = in.read()) > 0) {
			mainIndex++;

			if(record) this.recordBuffer.append((char)c);
			switch((char)c) {
			case '{' :
				if(this.quoted) {
					currentValue(c);
					break;
				}
				firstval = true;
				if((expect & OBJECT) == 0) throw new IOException("Unexpected { at index " + mainIndex);
				handler.startObject();

				status = pushStatus(OBJECT);
				expect = KEY;
				break;
			case '}' :
				if(this.quoted) {
					currentValue(c);
					break;
				}
				firstval = false;
				if((expect & VALUE) != 0) {
					handler.simpleValue(stripQuotes(this.currentValue.toString()));
					this.currentValue = new StringBuffer();
				}
				handler.endObject();
				
				status = popStatus();
				if(status == OBJECT) expect = KEY;
				else expect = OBJECT | ARRAY;
				break;
			case '[' :
				if(this.quoted) {
					currentValue(c);
					break;
				}
				firstval = true;
				if((expect & ARRAY) == 0) throw new IOException("Unexpected [ at index " + mainIndex);
				handler.startArray();
				
				status = pushStatus(ARRAY);
				expect = VALUE | OBJECT | ARRAY;
				break;
			case ']' :
				if(this.quoted) {
					currentValue(c);
					break;
				}
				firstval = false;
				if((expect & VALUE) != 0) {
					handler.simpleValue(stripQuotes(this.currentValue.toString()));
					this.currentValue = new StringBuffer();
				}
				handler.endArray();
				
				status = popStatus();
				
				expect = OBJECT | ARRAY;
				if(status == ARRAY) expect |= VALUE;
				else if(status == OBJECT) expect |= KEY;
				break;
			case ',' :
				if(this.quoted) {
					currentValue(c);
					break;
				}
				if(firstval && this.currentValue.length() <= 0) throw new IOException("Unexpected comma at index " + mainIndex);
				else if((expect & KEY) != 0 && (firstval || this.currentValue.length()>0)) throw new IOException("Unexpected comma at index " + mainIndex);
				else if((expect & VALUE) != 0) {
					if(! this.quoted) handler.simpleValue(stripQuotes(this.currentValue.toString()));
				}

				if(status == OBJECT) expect = KEY;
				else if(status == ARRAY) expect = VALUE | OBJECT | ARRAY;
				else throw new IOException("Unexpected comma");
				this.currentValue = new StringBuffer();
				firstval = false;
				break;
			case ':' :
				if(((expect & KEY) == 0 && !this.quoted) || this.currentValue.length() <= 0) throw new IOException("Unexpected colon at index " + mainIndex);
				if(! this.quoted) {
					handler.key(stripQuotes(this.currentValue.toString()));
					expect = VALUE | OBJECT | ARRAY;
					this.currentValue = new StringBuffer();
				} else currentValue(c);
				break;
			case ' ' :
			case '\t' :
				if((expect & VALUE) != 0 && this.currentValue.length() > 0) {
					currentValue(c);
				}
				// else ignore
				break;
			case '\r' :
			case '\n' :
				// ignore
				break;
			case '\"':
				if((prevc == '\\' && this.quoted)) currentValue(c);
				else this.quoted = !this.quoted;
				break;
			default :
				if((expect & (VALUE | KEY)) == 0) throw new IOException("Unexpected character: " + (char)c + " at index " + mainIndex);
				currentValue(c);
				break;
			}
			
			prevc = c;
		}
		
		handler.endParsing();
	}
	
	public void startRecording(String prefix) {
		this.recordBuffer.setLength(0);
		if(prefix != null) this.recordBuffer.append(prefix);
		this.record = true;
	}
	public boolean isRecording() { return this.record; }
	public String endRecording() {
		this.record = false;
		return this.recordBuffer.toString();
	}
	
		
	private int pushStatus(int status) throws IOException {
		if(this.level >= this.stack.length-1) throw new IOException("Stack overflow");
		this.stack[++ this.level] = status;
		return status;
	}
	
	private int popStatus() {
		if(this.level <= 0) {
			this.level = 0;
			return 0;
		}
		return this.stack[-- this.level ];
	}
	
	private void currentValue(int c) {
		this.currentValue.append((char)c);
	}

	private String stripQuotes(String val) {
    	val = val.trim();
    	if(val.startsWith("\"")) val = val.substring(1);
		if(val.endsWith("\"")) val = val.substring(0, val.length()-1);
		return val;
    }
	
}
