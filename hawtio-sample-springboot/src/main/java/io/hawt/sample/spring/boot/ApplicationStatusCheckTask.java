package io.hawt.sample.spring.boot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStatusCheckTask {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStatusCheckTask.class);
	
	@Autowired
	ApplicationsDao dao;
	
	@Autowired
	RestHelper helper;
	
	@Scheduled(fixedRate=30000l)
	public void checkStatus(){
		System.out.println("********* task ***********");
		List<Application> apps = dao.listApplications();
		for(Application app: apps){
			Double cpu= helper.getCpuUsage(app.hostName, app.jmxPort, app.jmxUsername, app.jmxPassword);
			if(cpu > 0.05){
				LOGGER.error("{} is under high cpu usage", app.name);
			}
			Double heap= helper.getHeapMemoryUsage(app.hostName, app.jmxPort, app.jmxUsername, app.jmxPassword);
			if(heap < 0.5){
				LOGGER.error("{} is under high heap usage", app.name);
			}
		}
	}

}
