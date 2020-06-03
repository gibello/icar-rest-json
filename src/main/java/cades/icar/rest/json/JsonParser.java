package cades.icar.rest.json;

import java.io.IOException;
import java.io.InputStream;

/**
 * JSON push parser (looks like XML sax parsing).
 * @author Pierre-Yves Gibello
 */
public class JsonParser {

	StringBuffer currentValue_ = new StringBuffer();
	int level_ = 0;
	int stack_[] = new int[512];
	boolean record_;
	StringBuilder recordBuffer_ = new StringBuilder();
	
	private static final int OBJECT = 1;
	private static final int ARRAY = 2;
	private static final int VALUE = 4;
	private static final int KEY = 8;

	public void parse(InputStream in, EventHandler handler) throws Exception {
		int expect = OBJECT | ARRAY;
		int status = 0, mainIndex = 0;;
		boolean firstval = true;
		int c;

		handler.startParsing(this);

		while((c = in.read()) > 0) {
			mainIndex++;

			if(record_) recordBuffer_.append((char)c);
			switch((char)c) {
			case '{' :
				firstval = true;
				if((expect & OBJECT) == 0) throw new IOException("Unexpected { at index " + mainIndex);
				handler.startObject();

				status = pushStatus(OBJECT);
				expect = KEY;
				break;
			case '}' :
				firstval = false;
				if((expect & VALUE) != 0) {
					handler.simpleValue(stripQuotes(currentValue_.toString()));
					currentValue_ = new StringBuffer();
				}
				handler.endObject();
				
				status = popStatus();
				if(status == OBJECT) expect = KEY;
				else {
					expect = OBJECT | ARRAY;
					if(status == ARRAY) expect |= VALUE;
				}
				break;
			case '[' :
				firstval = true;
				if((expect & ARRAY) == 0) throw new IOException("Unexpected [ at index " + mainIndex);
				handler.startArray();
				
				status = pushStatus(ARRAY);
				expect = VALUE | OBJECT | ARRAY;
				break;
			case ']' :
				firstval = false;
				if((expect & VALUE) != 0) {
					handler.simpleValue(stripQuotes(currentValue_.toString()));
					currentValue_ = new StringBuffer();
				}
				handler.endArray();
				
				status = popStatus();
				
				expect = OBJECT | ARRAY;
				if(status == ARRAY) expect |= VALUE;
				else if(status == OBJECT) expect |= KEY;
				break;
			case ',' :
				if(firstval && currentValue_.length() <= 0) throw new IOException("Unexpected comma at index " + mainIndex);
				else if((expect & KEY) != 0 && (firstval || currentValue_.length()>0)) throw new IOException("Unexpected comma at index " + mainIndex);
				else if((expect & VALUE) != 0) handler.simpleValue(stripQuotes(currentValue_.toString()));
				
				if(status == OBJECT) expect = KEY;
				else if(status == ARRAY) expect = VALUE | OBJECT | ARRAY;
				else throw new IOException("Unexpected comma");
				currentValue_ = new StringBuffer();
				firstval = false;
				break;
			case ':' :
				if((expect & KEY) == 0 || currentValue_.length() <= 0) throw new IOException("Unexpected colon at index " + mainIndex);
				handler.key(stripQuotes(currentValue_.toString()));
				expect = VALUE | OBJECT | ARRAY;
				currentValue_ = new StringBuffer();
				break;
			case ' ' :
			case '\t' :
				if((expect & VALUE) != 0 && currentValue_.length() > 0) {
					currentValue_.append((char)c);
				}
				// else ignore
				break;
			case '\r' :
			case '\n' :
				// ignore
				break;
			default :
				//System.out.println(status);
				if((expect & (VALUE | KEY)) == 0) throw new IOException("Unexpected character: " + (char)c + " at index " + mainIndex);
				currentValue_.append((char)c);
				break;
			}
		}
		
		handler.endParsing();
	}
	
	public void startRecording(String prefix) {
		recordBuffer_.setLength(0);
		if(prefix != null) recordBuffer_.append(prefix);
		record_ = true;
	}
	public boolean isRecording() { return record_; }
	public String endRecording() {
		record_ = false;
		return recordBuffer_.toString();
	}
	
		
	private int pushStatus(int status) throws IOException {
		if(level_ >= stack_.length-1) throw new IOException("Stack overflow");
		stack_[++ level_] = status;
		return status;
	}
	
	private int popStatus() {
		if(level_ <= 0) {
			level_ = 0;
			return 0;
		}
		return stack_[-- level_ ];
	}
	
	private String stripQuotes(String val) {
    	val = val.trim();
    	if(val.startsWith("\"")) val = val.substring(1);
		if(val.endsWith("\"")) val = val.substring(0, val.length()-1);
		return val;
    }

}
