angular.module('app')
.controller('nalogController', ['$scope','nalogService', function($scope, nalogService){
	
	
	$scope.nalog = {};
	
	$scope.posaljiNalog = function(){
		console.log($scope.nalog.hitno);
		$scope.nalog.idPoruke = Math.random().toString(36).substring(7);
		if($scope.nalog.hitno === undefined){
			$scope.nalog.hitno = false;
		}
		var datumNaloga = $scope.nalog.datumNaloga.getDate()+"-"+($scope.nalog.datumNaloga.getMonth()+1)+"-"+$scope.nalog.datumNaloga.getFullYear();
		$scope.nalog.datumNaloga = datumNaloga;
		
		var datumValute = $scope.nalog.datumValute.getDate()+"-"+($scope.nalog.datumValute.getMonth()+1)+"-"+$scope.nalog.datumValute.getFullYear();
		$scope.nalog.datumValute = datumValute;
		nalogService.posaljiNalog($scope.nalog)
			.then(function(response) {
				console.log("uspesno poslat");
			}
			, function(error) {
				console.log("greska pri slanju");
			})
		
		console.log($scope.nalog);
		$scope.nalog = {};
	}
	
}]);