package cades.icar.rest.json.sample;

import java.io.ByteArrayInputStream;

import cades.icar.rest.json.PrettyPrintHandler;
import cades.icar.rest.json.JsonParser;

/**
 * Sample parsing JSON metric out of SonarQube API.
 * @author Pierre-Yves Gibello
 */
public class SonarMetricsSample {

	public static void main(String args[]) throws Exception {
		JsonParser parser = new JsonParser();
	    PrettyPrintHandler handler = new PrettyPrintHandler(System.out);
	    String json = "{\"component\":{\"id\":\"AW4X78189JQdTRImNmj4\",\"key\":\"org.xwiki.platform:xwiki-platform\",\"name\":\"XWiki Platform - Parent POM\",\"description\":\"XWiki Platform - \\\"Official\\\" parent POM\",\"qualifier\":\"TRK\",\"measures\":[{\"metric\":\"test_success_density\",\"value\":\"100.0\",\"bestValue\":true},{\"metric\":\"new_coverage\",\"periods\":[{\"index\":1,\"value\":\"38.5964912280702\",\"bestValue\":false}]}]}}";
		parser.parse(new ByteArrayInputStream(json.getBytes()), handler);
	}
}
