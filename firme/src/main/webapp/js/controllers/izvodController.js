angular.module('app')
.controller('izvodController', ['$scope','izvodService', function($scope, izvodService){
	$scope.izvod = {};
	
	$scope.preseci = [];
	
	
	$scope.posaljiZahtevZaIzvod = function(){
		console.log("poziva funkciju");
		izvodService.posaljiNalogZaIzvod($scope.izvod).then(function(response){
			
			$scope.preseci.push(response.data);
			console.log($scope.preseci);
		})
	}
}]);