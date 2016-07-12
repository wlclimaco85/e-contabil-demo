(function() {
  'use strict';
	var commonControllers =  angular.module('wdApp.controllers', []);

	commonControllers.controller('WDAppController', ['$scope', '$rootScope', function($scope, $rootScope) {
		var $window;
		$window = $(window);

		$scope.admin = {
			layout: 'wide',
			menu: 'vertical',
			fixedHeader: true,
			fixedSidebar: true
		};

		$scope.$watch('admin', function(newVal, oldVal) {
			if (newVal.menu === 'vertical' && oldVal.menu === 'horizontal') {
			  $rootScope.$broadcast('nav:reset');
			  return;
			}
			if (newVal.fixedHeader === false && newVal.fixedSidebar === true) {
			  if (oldVal.fixedHeader === false && oldVal.fixedSidebar === false) {
				$scope.admin.fixedHeader = true;
				$scope.admin.fixedSidebar = true;
			  }
			  if (oldVal.fixedHeader === true && oldVal.fixedSidebar === true) {
				$scope.admin.fixedHeader = false;
				$scope.admin.fixedSidebar = false;
			  }
			  return;
			}
			if (newVal.fixedSidebar === true) {
			  $scope.admin.fixedHeader = true;
			}
			if (newVal.fixedHeader === false) {
			  $scope.admin.fixedSidebar = false;
			}
		}, true);

		return $scope.color = {
			primary: '#00475B',
			success: '#94B758',
			info: '#56BDF1',
			infoAlt: '#7F6EC7',
			warning: '#F3C536',
			danger: '#FFDC54'
		 };
    }]);

	commonControllers.controller('NavContainerController', ['$scope', function($scope) {}]);

	commonControllers.controller('NavController', ['$scope', 'TaskStorage', 'filterFilter','localStorageService', function($scope, TaskStorage, filterFilter,localStorageService) {
		var vm = this;
		var tasks;

		vm.test = 0;
	  $scope.empresa = {
        types: []
      };

      $scope.empresaType = 1

      function visible(){
      	debugger
      	return true
      }

      $scope.change = function() {
       debugger
      };

      $scope.submit = function() {
	      if($scope.empresaType == 1)
	      {
	      		$('.fiscal').hide();	
	      }
      };

      $('.clinica').hide();	
      $('.advocacia').hide();	
      $('.condominio').hide();	

		tasks = $scope.tasks = TaskStorage.get();
		$scope.taskRemainingCount = filterFilter(tasks, {
			completed: false
		}).length;

		
		return $scope.empresaType;
    }]);

	commonControllers.controller('DashboardController', ['$scope', function($scope) {
		
	}]);

	commonControllers.controller('LoginController', ['$scope', '$rootScope', '$location', 'localStorageService','WDAuthentication',
		function($scope, $rootScope, $location, localStorageService, WDAuthentication) {

			$scope.login = function() {
				WDAuthentication.processLogin(WebDaptiveAppConfig.authenticationURL, $.param({username: $scope.username, password: $scope.password}), function(authenticationResult) {
					var authToken = authenticationResult.token;
					if (authToken !== undefined){
						$rootScope.authToken = authToken;
						localStorageService.set('authToken', authToken);
						var currentUser = {user: authenticationResult.name, roles: authenticationResult.roles};
						$rootScope.user = currentUser;
						$rootScope.main.name = authenticationResult.name;
						localStorageService.set('currentUser', $rootScope.user);
						var tempRole = "";
						for (var prop in authenticationResult.roles) {
							tempRole += prop + " ";
						}
						$rootScope.displayRoles = tempRole;
						localStorageService.set('displayRoles', $rootScope.displayRoles);
						if ($rootScope.callingPath !== undefined){
							if ($rootScope.callingPath === '/pages/signin'){
								$rootScope.callingPath = "/principal";

							}
							$location.path($rootScope.callingPath);
						}
						else{
							$location.path( "/dashboard" );
						}
					}
					else{
							$location.path( "/pages/signin" );
					}
				});
			};
    }]);

})();
