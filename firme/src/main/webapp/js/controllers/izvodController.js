angular.module('app')
.controller('izvodController', ['$scope','izvodService', '$state', function($scope, izvodService, $state){
	$scope.izvod = {};
	
	$scope.preseci = [];
	$scope.noResult = false;
	
	$scope.posaljiZahtevZaIzvod = function() {
		izvodService.posaljiNalogZaIzvod($scope.izvod)
			.then(function(response) {
				if((response.data != "") && (response != undefined)){
					$scope.preseci.push(response.data);
					$scope.noResult = false;
				} else if(response.data == ""){
					$scope.noResult = true;
			 	}
				console.log($scope.preseci);

			}, function(error) {
				if (error.status == 401 || error.status == 403) {
					$state.go("login");
				}
			})
	}
}]);