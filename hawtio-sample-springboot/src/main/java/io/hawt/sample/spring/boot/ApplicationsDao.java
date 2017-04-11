package io.hawt.sample.spring.boot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationsDao {

	public static final String LIST = "select application, scheme, host_name, jmx_port, jolokia_path, jmx_username, jmx_password from monitoring_applications";

	public static final String LOGS_QUERY = "INSERT INTO MONITORING_LOGS(MONITORING_LOGS_KEY,APPLICATION,LOG_MESSAGE,RESOLVED_STATUS,CREATED_DTTM,MODIFIED_DTTM) VALUES (MONITORING_LOGS_SEQ.nextval,:application,:logMessage,:status,sysdate,sysdate)";

	public static final String ALERTS_QUERY = "select application,log_message,resolved_status from MONITORING_LOGS";

	@Autowired
	NamedParameterJdbcTemplate template;

	public List<Application> listApplications() {
		final List<Application> apps = new ArrayList<Application>();
		template.query(LIST, new MapSqlParameterSource(), new RowMapper<Application>() {

			@Override
			public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
				Application app = new Application();
				app.name = rs.getString(1);
				app.scheme = rs.getString(2);
				app.hostName = rs.getString(3);
				app.jmxPort = rs.getString(4);
				app.jolokiaPath = rs.getString(5);
				app.jmxUsername = rs.getString(6);
				app.jmxPassword = rs.getString(7);
				apps.add(app);
				return app;
			}
		});
		return apps;
	}
	
	public int logMessagesIntoDB(String appName, String errorMessage) {
		Map namedParameters = new HashMap();
		namedParameters.put("application", appName);
		namedParameters.put("logMessage", errorMessage);
		namedParameters.put("status", "N");
		return template.update(LOGS_QUERY, namedParameters);
	}

	public List<Alert> listAlerts() {

		final List<Alert> alerts = new ArrayList<Alert>();
		template.query(ALERTS_QUERY, new MapSqlParameterSource(), new RowMapper<Alert>() {
			@Override
			public Alert mapRow(ResultSet rs, int rowNum) throws SQLException {
				Alert alert = new Alert();
				alert.application = rs.getString(1);
				alert.logMessage = rs.getString(2);
				alert.resolvedStatus = rs.getString(3);
				alerts.add(alert);
				return alert;
			}
		});

		return alerts;
	}

}
