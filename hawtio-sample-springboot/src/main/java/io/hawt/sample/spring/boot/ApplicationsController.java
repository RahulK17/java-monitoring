package io.hawt.sample.spring.boot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationsController {

	@Autowired
	ApplicationsDao dao;

	@Autowired
	RestHelper helper;

	@RequestMapping("/hawtio/custom/applications-list")
	public @ResponseBody List<Application> getApplications() {
		return dao.listApplications();
	}

	@RequestMapping("/hawtio/custom/applications-status")
	public @ResponseBody List<ApplicationStatus> getApplicationsStatus() {
		List<Application> apps = dao.listApplications();
		List<ApplicationStatus> statuses = new ArrayList<ApplicationStatus>();
		for (Application app : apps) {
			ApplicationStatus status = new ApplicationStatus();
			status.name = app.name;
			status.cpu = String
					.valueOf(helper.getCpuUsage(app.hostName, app.jmxPort, app.jmxUsername, app.jmxPassword));
			status.heap = String
					.valueOf(helper.getHeapMemoryUsage(app.hostName, app.jmxPort, app.jmxUsername, app.jmxPassword));
			statuses.add(status);
		}
		return statuses;
	}

}
