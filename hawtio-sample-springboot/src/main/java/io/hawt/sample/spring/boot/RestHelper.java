package io.hawt.sample.spring.boot;

import java.nio.charset.Charset;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

@Component
public class RestHelper {
	
	@Autowired
	RestTemplate template;
	
	public Double getCpuUsage(Application app){
		
		String json = template.exchange(app.scheme+"://"+app.hostName+":"+app.jmxPort+"/"+app.jolokiaPath+"/read/java.lang:type=OperatingSystem/SystemCpuLoad",
				HttpMethod.GET,
				getHttpEntity(getAuthorization(app)),
				String.class).getBody();
		return new JSONObject(json).getDouble("value");
	}
	
	public Double getHeapMemoryUsage(Application app){
		
		String json = template.exchange(app.scheme+"://"+app.hostName+":"+app.jmxPort+"/"+app.jolokiaPath+"/read/java.lang:type=Memory/HeapMemoryUsage",
				HttpMethod.GET,
				getHttpEntity(getAuthorization(app)),
				String.class).getBody();
		
		JSONObject obj =  new JSONObject(json);
		Double max = obj.getJSONObject("value").getDouble("max");
		Double used = obj.getJSONObject("value").getDouble("used");
		return (max-used)/max;
	}
	
	public HttpHeaders getAuthorization(Application app){
		return new HttpHeaders() {{
	         String auth = app.jmxUsername + ":" + app.jmxPassword;
	         byte[] encodedAuth = Base64Utils.encode( 
	            auth.getBytes(Charset.forName("US-ASCII")));
	         String authHeader = "Basic " + new String( encodedAuth );
	         set( "Authorization", authHeader );
	      }};
	}
	
	public HttpEntity getHttpEntity(HttpHeaders headers){
		return new HttpEntity(headers);
	}
}
