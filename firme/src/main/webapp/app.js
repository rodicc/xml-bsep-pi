var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', function($stateProvider){
	
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
	
}]);