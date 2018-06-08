var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', function($stateProvider){
	
	$stateProvider
	
	.state('sertifikat',{
		url:'/sertifikat',
		templateUrl: 'view/noviSertifikat.html',
		controller: 'certificateController'
	})
	
	
	
}]);