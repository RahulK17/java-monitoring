var ApplicationsPlugin = (function(ApplicationsPlugin) {

	ApplicationsPlugin.pluginName = 'applications-plugin';
	ApplicationsPlugin.log = Logger.get('ApplicationsPlugin');
	ApplicationsPlugin.contextPath = "/hawtio/plugins/";
	ApplicationsPlugin.templatePath = ApplicationsPlugin.contextPath + "applications-plugin/html/";

	ApplicationsPlugin.module = angular.module('applications-plugin', ['hawtioCore'])
      .config(function($routeProvider) {
        $routeProvider.
            when('/applications-plugin', {
              templateUrl: ApplicationsPlugin.templatePath + 'applications-plugin.html'
            });
      });

	ApplicationsPlugin.module.run(function(workspace, viewRegistry, layoutFull) {

	ApplicationsPlugin.log.info(ApplicationsPlugin.pluginName, " loaded");
    viewRegistry["applications-plugin"] = layoutFull;
    workspace.topLevelTabs.push({
      id: "ApplicationsPlugin",
      content: "Applications",
      title: "Applications Monitored",
      isValid: function() { return true; },
      href: function() { return "#/applications-plugin"; },
      isActive: function() { return true; }

    });

  });

	ApplicationsPlugin.ApplicationsPluginController = function($scope, $http, localStorage) {
	
		// connect link click function
		$scope.connect = function(app){
			Core.connectToServer(localStorage, Core.getConnectOptions(app.name));
		};
		
		$http.get("/hawtio/custom/applications-status").then(
    		function(response){
    			// load connection map from local storage
    			var connectionMap = Core.loadConnectionMap();
    			for(var i=0, l=response.data.length;i<l;i++){
    				var item = response.data[i];
    				connectionMap[item.name] = newConfig(item.application.scheme,item.application.hostName,
    						item.application.jmxPort,item.application.jolokiaPath,item.application.jmxUsername,
    						item.application.jmxPassword);
    			}
    			
    			// save connection map
    			Core.saveConnectionMap(connectionMap);
    			
    			//update the table
    			$scope.applications = response.data;
    			
    		});
		Core.$apply($scope); 
    
		function newConfig(schm, hst, prt, pth, uname, passwd) {
	        return Core.createConnectOptions({
	            scheme: schm,
	            host: hst,
	            path: pth,
	            port: prt,
	            userName: uname,
	            password: passwd
	        });
		};
	}
  return ApplicationsPlugin;

})(ApplicationsPlugin || {});

hawtioPluginLoader.addModule(ApplicationsPlugin.pluginName);