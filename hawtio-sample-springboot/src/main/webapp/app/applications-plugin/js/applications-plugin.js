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

	ApplicationsPlugin.ApplicationsPluginController = function($scope, $http) {
    $http.get("/hawtio/custom/applications-status").then(
    		function(response){
    			$scope.applications = response.data;
    		});
    Core.$apply($scope);    
  };

  return ApplicationsPlugin;

})(ApplicationsPlugin || {});

hawtioPluginLoader.addModule(ApplicationsPlugin.pluginName);