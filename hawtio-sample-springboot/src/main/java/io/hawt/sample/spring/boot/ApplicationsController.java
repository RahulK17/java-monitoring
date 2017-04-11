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
			status.application = app; // for connect option in ui
			status.name = app.name;
			status.cpu = String
					.valueOf((int)(helper.getCpuUsage(app)*100));
			status.heap = String
					.valueOf((int)(helper.getHeapMemoryUsage(app)*100));
			statuses.add(status);
		}
		return statuses;
	}

}
