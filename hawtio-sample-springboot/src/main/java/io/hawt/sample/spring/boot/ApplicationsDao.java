package io.hawt.sample.spring.boot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationsDao {

	public static final String LIST = "select application 'name', host_name 'hostName', jmx_port 'jmxPart', jmx_username 'jmxUsername', jmx_password 'jmxPassword' from monitoring_applications";
	
	@Autowired
	NamedParameterJdbcTemplate template;
	
	public List<Application> listApplications(){
		final List<Application> apps = new ArrayList<Application>();
		template.query(LIST, new MapSqlParameterSource(),new RowMapper<Application>() {

			@Override
			public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
				Application app = new Application();
				app.name = rs.getString(1);
				app.hostName = rs.getString(2);
				app.jmxPort = rs.getString(3);
				app.jmxUsername = rs.getString(4);
				app.jmxPassword = rs.getString(5);
				apps.add(app);
				return app;
			}
		});
		return apps;
	}
	
	
}
