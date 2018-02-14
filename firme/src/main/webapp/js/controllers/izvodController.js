angular.module('app')
.controller('izvodController', ['$scope','izvodService', function($scope, izvodService){
	$scope.izvod = {};
	
	$scope.preseci = [];
	$scope.noResult = false;
	
	$scope.posaljiZahtevZaIzvod = function(){
		console.log("poziva funkciju");
		izvodService.posaljiNalogZaIzvod($scope.izvod).then(function(response){
			 if((response.data != "") && (response != undefined)){
					$scope.preseci.push(response.data);
					$scope.noResult = false;
			 }
			 else if(response.data == ""){
				 $scope.noResult = true;
			 }
			
			console.log($scope.preseci);
		})
	}
}]);