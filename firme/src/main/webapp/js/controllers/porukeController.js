angular.module("app")
.controller('porukeController', ['$scope', '$rootScope', 'porukeService', 
	function($scope, $rootScope, porukeService){
		
	poruke = {};
	
	$scope.prikaziSvePoruke = function(){
		porukeService.prikaziSvePoruke().then(function(response){
			$scope.poruke = response.data;
			console.log($scope.poruke);
		});
	}
	
	$scope.prikaziSvePoruke();
	
}])