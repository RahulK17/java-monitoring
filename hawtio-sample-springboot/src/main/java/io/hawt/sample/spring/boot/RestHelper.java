package io.hawt.sample.spring.boot;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestHelper {
	
	@Autowired
	RestTemplate template;
	
	public Double getCpuUsage(Application app){
		String json = template.getForObject(app.scheme+"://"+app.hostName+":"+app.jmxPort+"/"+app.jolokiaPath+"/read/java.lang:type=OperatingSystem/SystemCpuLoad", String.class);
		return new JSONObject(json).getDouble("value");
	}
	
	public Double getHeapMemoryUsage(Application app){
		String json = template.getForObject(app.scheme+"://"+app.hostName+":"+app.jmxPort+"/"+app.jolokiaPath+"/read/java.lang:type=Memory/HeapMemoryUsage", String.class);
		JSONObject obj =  new JSONObject(json);
		Double max = obj.getJSONObject("value").getDouble("max");
		Double used = obj.getJSONObject("value").getDouble("used");
		return (max-used)/max;
	}
	
}
