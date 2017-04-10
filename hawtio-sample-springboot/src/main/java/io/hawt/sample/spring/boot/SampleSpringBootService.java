package io.hawt.sample.spring.boot;

import io.hawt.config.ConfigFacade;
import io.hawt.springboot.EnableHawtio;
import io.hawt.springboot.HawtPlugin;
import io.hawt.springboot.PluginService;
import io.hawt.system.ConfigManager;
import io.hawt.web.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

@SpringBootApplication
@EnableHawtio
@EnableScheduling
public class SampleSpringBootService {

	@Autowired
	private ServletContext servletContext;

    public static void main(String[] args) {
		System.setProperty(AuthenticationFilter.HAWTIO_AUTHENTICATION_ENABLED, "false");
		SpringApplication.run(SampleSpringBootService.class, args);
    }

	@PostConstruct
	public void init() {
		final ConfigManager configManager = new ConfigManager();
		configManager.init();
		servletContext.setAttribute("ConfigManager", configManager);
	}

	@Bean
	public HawtPlugin applicationsPlugin() {
		return new HawtPlugin("applications-plugin", "/hawtio/plugins", "", new String[] { "applications-plugin/js/applications-plugin.js" });
	}
	
	/**
	 * Set things up to be in offline mode
	 * @return
	 * @throws Exception
	 */
	@Bean
	public ConfigFacade configFacade() throws Exception {
		ConfigFacade config = new ConfigFacade() {
			public boolean isOffline() {
				return true;
			}
		};
		config.init();
		return config;
	}
	
	/**
	 * Register rest endpoint to handle requests for /plugin, and return all registered plugins.
	 * @return
	 */
	@Bean
	public PluginService pluginService(){
		return new PluginService();
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
