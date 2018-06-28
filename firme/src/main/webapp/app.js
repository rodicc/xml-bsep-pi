var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', '$httpProvider', function($stateProvider, $httpProvider) {
	
	$stateProvider
		.state('faktura',{
			url:'/faktura',
			templateUrl: 'view/faktura.html',
			controller: 'fakturaController'
			/*	,
			resolve : {
<<<<<<< HEAD
				//authenticate: authenticateCb
			}
=======
				authenticate: authenticateCb
			}*/
>>>>>>> 065c65426140c0d2e4cf191e3408bf2e2612f88b
		})
		
		.state('nalog',{
			url:'/nalog',
			templateUrl:'view/nalog.html',
			controller:'nalogController'
		/*		,
			resolve : {
<<<<<<< HEAD
				//authenticate: authenticateCb
			}
=======
				authenticate: authenticateCb
			}*/
>>>>>>> 065c65426140c0d2e4cf191e3408bf2e2612f88b
		})
		
		.state('izvod',{
			url:'/izvod',
			templateUrl:'view/izvod.html',
			controller:'izvodController'
		/*		,
			resolve : {
<<<<<<< HEAD
				//authenticate: authenticateCb
			}
=======
				authenticate: authenticateCb
			}*/
>>>>>>> 065c65426140c0d2e4cf191e3408bf2e2612f88b
		})
		
		.state('poruke',{
			url:'/poruke',
			templateUrl:'view/poruke.html',
			controller:'porukeController'
			/*	,
			resolve : {
<<<<<<< HEAD
				//authenticate: authenticateCb
			}
=======
				authenticate: authenticateCb
			}*/
>>>>>>> 065c65426140c0d2e4cf191e3408bf2e2612f88b
		})
		
		.state('fakture',{
			url:'/fakture',
			templateUrl: 'view/pregledFaktura.html',
			controller: 'pregledFakturaController'
			/*	,
			resolve : {
<<<<<<< HEAD
				//authenticate: authenticateCb
			}
=======
				authenticate: authenticateCb
			}*/
>>>>>>> 065c65426140c0d2e4cf191e3408bf2e2612f88b
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
		})
		
		.state('otherwise', {
			url: '*path',
			templateUrl: 'view/login.html',
			controller: 'userController'
		/*		,
			resolve : {
<<<<<<< HEAD
				//authenticate: authenticateCb
			}
=======
				authenticate: authenticateCb
			}*/
>>>>>>> 065c65426140c0d2e4cf191e3408bf2e2612f88b
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
	
}])

/*
var authenticateCb = function (AuthenticatorService) {
      return AuthenticatorService.authenticated();
    };

var redirectService = function($q, $rootScope, $location) {

    this.authenticated = function () {
      var deferred = $q.defer();
      if ($rootScope.user) {
        deferred.resolve();
      } else {
        $location.path("/login");
        deferred.reject();
      }
      return deferred.promise;
    };
  }*/


