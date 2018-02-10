angular.module('app')
.controller('nalogController', ['$scope','nalogService', function($scope, nalogService){
	
	
	$scope.nalog = {};
	
	$scope.posaljiNalog = function(){
		nalogService.posaljiNalog($scope.nalog).then(function(response){
			
		})
		$scope.nalog = {};
	}
	
}]);