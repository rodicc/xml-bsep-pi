var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', '$httpProvider', function($stateProvider, $httpProvider){
	
	$stateProvider
	
	.state('sertifikat',{
		url:'/sertifikat',
		templateUrl: 'view/noviSertifikat.html',
		controller: 'certificateController'
	})
	
	.state('ukidanje',{
		url:'/sertifikat/ukidanje',
		templateUrl: 'view/ukidanjeSertifikata.html',
		controller: 'certificateController'
	})
	
	.state('provera',{
		url:'/sertifikat/provera',
		templateUrl: 'view/proveraSertifikata.html',
		controller: 'certificateController'
	})
	
	.state('login', {
			url: '/login',
			templateUrl: 'view/login.html',
			controller: 'userController'
	})

	.state('register', {
		url: '/register',
		templateUrl: 'view/registration.html',
		controller: 'userController'
	});

	$httpProvider.interceptors.push(["$q", "$injector", "$rootScope", function($q, $injector, $rootScope) {
		return {
			request: function(config) {
				var token = localStorage.getItem("Authorization");
				if (token) {
					config.headers["Authorization"] = token;
					$rootScope.user = localStorage.getItem("User");
					$rootScope.role = localStorage.getItem("Role");
				}
				return config;
			}
		};
	}]);	
}]);
