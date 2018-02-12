angular.module("app")
.controller('porukeController', ['$scope', '$rootScope', 'porukeService', 
	function($scope, $rootScope, porukeService){
		
	$scope.prikaziSvePoruke = function(){
		porukeService.prikaziSvePoruke().then(function(response){
			$rootScope.poruke = response.data;
			console.log($rootScope.poruke);
		});
	}
	
	
}])