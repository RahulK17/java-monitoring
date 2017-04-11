var AlertPlugin = (function(AlertPlugin) {

	AlertPlugin.pluginName = 'alert-plugin';
	AlertPlugin.log = Logger.get('AlertPlugin');
	AlertPlugin.contextPath = "/hawtio/plugins/";
	AlertPlugin.templatePath = AlertPlugin.contextPath + "alert-plugin/html/";

	AlertPlugin.module = angular.module('alert-plugin', ['hawtioCore'])
      .config(function($routeProvider) {
        $routeProvider.
            when('/alert-plugin', {
              templateUrl: AlertPlugin.templatePath + 'alert-plugin.html'
            });
      });

	AlertPlugin.module.run(function(workspace, viewRegistry, layoutFull) {

		AlertPlugin.log.info(AlertPlugin.pluginName, " loaded");
    viewRegistry["alert-plugin"] = layoutFull;
    workspace.topLevelTabs.push({
      id: "AlertPlugin",
      content: "Alerts",
      title: "Alerts Triggered",
      isValid: function() { return true; },
      href: function() { return "#/alert-plugin"; },
      isActive: function() { return true; }

    });

  });

	AlertPlugin.AlertPluginController = function($scope, $http, localStorage) {
	
		// connect link click function
		$scope.connect = function(app){
			Core.connectToServer(localStorage, Core.getConnectOptions(app.name));
		};
		
		$http.get("/hawtio/custom/alert-status").then(
    		function(response){
    			//update the table
    			$scope.alerts = response.data;
    			
    		});
		Core.$apply($scope); 
    
		
	}
  return AlertPlugin;

})(AlertPlugin || {});

hawtioPluginLoader.addModule(AlertPlugin.pluginName);