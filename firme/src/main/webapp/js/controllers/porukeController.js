angular.module("app")
.controller('porukeController', ['$scope', 'porukeService', '$state', 
	function($scope, porukeService, $state) {
		
	poruke = {};
	
	$scope.prikaziSvePoruke = function() {
		porukeService.prikaziSvePoruke()
			.then(function(response) {
				$scope.poruke = response.data;
			}, function(error) {
				if (error.status == 401 || error.status == 403) {
					$state.go("login");
				}
			});
	}
	$scope.prikaziSvePoruke();
	
}])