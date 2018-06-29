angular.module("app")
.controller('porukeController', ['$scope', 'porukeService', '$state', 
	function($scope, porukeService, $state) {
		
	poruke = {};
	
	$scope.prikaziSvePoruke = function() {
		porukeService.prikaziSvePoruke()
			.then(function(response) {
				$scope.poruke = response.data;
			}, function(error) {
			});
	}
	$scope.prikaziSvePoruke();
	
}])