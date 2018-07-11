var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', '$httpProvider', function($stateProvider, $httpProvider) {
	
	$stateProvider
		.state('noviKlijent',{
			url:'/noviKlijent',
			templateUrl: 'views/noviKlijent.html',
			controller: 'Controller',
			
		})
		
		.state('noviRacun',{
			url:'/noviRacun',
			templateUrl:'views/noviRacun.html',
			controller:'Controller',
			
		})
		
		.state('klijenti',{
			url:'/klijenti',
			templateUrl:'views/klijenti.html',
			controller:'Controller',
			
		})
		
		.state('racuni',{
			url:'/racuni',
			templateUrl:'views/racuni.html',
			controller:'Controller',
			
		})
		
		.state('ukinutiRacuni',{
			url:'/ukinutiRacuni',
			templateUrl:'views/ukinutiRacuni.html',
			controller:'Controller',
			
		});
		
}]);


