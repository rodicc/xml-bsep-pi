var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', '$httpProvider', function($stateProvider, $httpProvider, redirectService) {
	
	$stateProvider
		.state('faktura',{
			url:'/faktura',
			templateUrl: 'view/faktura.html',
			controller: 'fakturaController',
			resolve : {
				authenticate: authenticateCb
			}
		})
		
		.state('nalog',{
			url:'/nalog',
			templateUrl:'view/nalog.html',
			controller:'nalogController',
			resolve : {
				authenticate: authenticateCb
			}
		})
		
		.state('izvod',{
			url:'/izvod',
			templateUrl:'view/izvod.html',
			controller:'izvodController',
			resolve : {
				authenticate: authenticateCb
			}
		})
		
		.state('poruke',{
			url:'/poruke',
			templateUrl:'view/poruke.html',
			controller:'porukeController',
			resolve : {
				authenticate: authenticateCb
			}
		})
		
		.state('fakture',{
			url:'/fakture',
			templateUrl: 'view/pregledFaktura.html',
			controller: 'pregledFakturaController',
			resolve : {
				authenticate: authenticateCb
			}
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
		})
		/*	
		.state('otherwise', {
			url: '*path',
			templateUrl: 'view/login.html',
			controller: 'userController'
			,
			resolve : {
				authenticate: authenticateCb
			
		})}*/
		
		.state('profile', {
			url: '/profile',
			templateUrl: 'view/profile.html',
			controller: 'userUpdateController'
		});
		

	$httpProvider.interceptors.push(function($rootScope) {
		return {
			request: function(config) {
				$rootScope.user = localStorage.getItem("User");
				return config;
			}
		}
	})
	
}])


var authenticateCb = function (redirectService) {
      return redirectService.authenticated();
    };
/*
var redirectService = function($q, $rootScope, $location, $state) {

   this.authenticated = function () {
      var deferred = $q.defer();
      if ($rootScope.user == null) {
    	  //$location.path("/login");
    	  $state.go("login")
          deferred.reject();
    	  
      } else {
          deferred.resolve();
      }
      return deferred.promise;
    };
  }*/


