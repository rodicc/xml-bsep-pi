var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', '$httpProvider', function($stateProvider, $httpProvider) {
	
	$stateProvider
		
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
			
		})
		
		.state('izvestaji',{
			url:'/izvestaji',
			templateUrl:'views/izvestaji.html',
			controller:'Controller',
			
		});
		
}]);


