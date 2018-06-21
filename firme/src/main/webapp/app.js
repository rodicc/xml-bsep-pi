var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', '$httpProvider', function($stateProvider, $httpProvider) {
	
	$stateProvider
		.state('faktura',{
			url:'/faktura',
			templateUrl: 'view/faktura.html',
			controller: 'fakturaController'
		})
		
		.state('nalog',{
			url:'/nalog',
			templateUrl:'view/nalog.html',
			controller:'nalogController'
		})
		
		.state('izvod',{
			url:'/izvod',
			templateUrl:'view/izvod.html',
			controller:'izvodController'
		})
		
		.state('poruke',{
			url:'/poruke',
			templateUrl:'view/poruke.html',
			controller:'porukeController'
		})
		
		.state('fakture',{
			url:'/fakture',
			templateUrl: 'view/pregledFaktura.html',
			controller: 'pregledFakturaController'
		})
		
		.state('login', {
			url: '/login',
			templateUrl: 'view/login.html',
			controller: 'userController'
		})

		.state('register', {
			url: 'register',
			templateUrl: 'view/registration.html',
			controller: 'userController'
		});

	$httpProvider.interceptors.push(function($q, $injector, $rootScope) {
		return {
			request: function(config) {
				var token = localStorage.getItem("Authorization");
				if (token) {
					config.headers["Authorization"] = token;
					$rootScope.user = localStorage.getItem("User");
				}
				return config;
			}
		}
	})
}]);

