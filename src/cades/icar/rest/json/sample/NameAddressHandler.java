package cades.icar.rest.json.sample;

import java.io.ByteArrayInputStream;

import cades.icar.rest.json.JsonParser;

public class NameAddressHandler extends cades.icar.rest.json.DefaultHandler {
	
	String name_;
	String city_;
	String country_;
	
	String lastKey_;
	int level_;
	
	@Override
	public void startObject() {
		level_ ++;
	}

	@Override
	public void endObject() {
		level_ --;
	}

	@Override
	public void key(String key) {
		lastKey_ = key;
	}

	@Override
	public void simpleValue(String val) throws Exception {
		if("name".equals(lastKey_)) {
			if(level_ > 1) throw new Exception("Syntax error: name should be at 1st level");
			name_ = val;
		}
		else if("city".equals(lastKey_)) city_ = val;
		else if("country".equals(lastKey_)) country_ = val;
	}

	public NameAddress getResult() {
		return new NameAddress(name_, city_, country_);
	}

	public static void main(String args[]) throws Exception {
		JsonParser parser = new JsonParser();
	    NameAddressHandler handler = new NameAddressHandler();
	    String json = (args.length > 0 ? args[0] : "{ name: \"Oscar Wilde\", address: { city: \"London\", country: \"UK\" }}" );
		parser.parse(new ByteArrayInputStream(json.getBytes()), handler);
	    System.out.println(handler.getResult());
	}
}

class NameAddress {

	String name_;
	String city_;
	String country_;

	public NameAddress(String name, String city, String country) {
		this.name_ = name;
		this.city_ = city;
		this.country_ = country;
	}

	public String toString() {
		return name_ + " lives in " + city_ + ", " + country_;
	}
}
