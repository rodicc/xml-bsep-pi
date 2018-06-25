var app = angular.module('app', ['ui.router']);

app.config(['$stateProvider', function($stateProvider){
	
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
	
	
	
}]);