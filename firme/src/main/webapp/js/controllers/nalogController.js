angular.module('app')
.controller('nalogController', ['$scope','nalogService', function($scope, nalogService){
	
	$scope.obavestenja = {
		uspesno: "Nalog je uspeno poslan.",
		neuspesno: "Uneti podaci nisu korektni.",
		validan: false,
		nevalidan: false
	}
	$scope.nalog = {};

	$scope.obrisiPolja = function(){
		$scope.nalog = {};
	}
	
	$scope.posaljiNalog = function(){
		$scope.nalog.idPoruke = Math.random().toString(36).substring(7);
		if($scope.nalog.hitno === undefined){
			$scope.nalog.hitno = false;
		}
		var datumNaloga = $scope.datumNaloga.getDate()+"-"+($scope.datumNaloga.getMonth()+1)+"-"+$scope.datumNaloga.getFullYear();
		$scope.nalog.datumNaloga = datumNaloga;
		
		var datumValute = $scope.datumValute.getDate()+"-"+($scope.datumValute.getMonth()+1)+"-"+$scope.datumValute.getFullYear();
		$scope.nalog.datumValute = datumValute;
		nalogService.posaljiNalog($scope.nalog)
			.then(function(response) {
				$scope.obavestenja.validan = true;
				$scope.obavestenja.nevalidan = false;
				//$scope.nalog = {};
			}
			, function(error) {
				$scope.obavestenja.validan = false;
				$scope.obavestenja.nevalidan = true;
				//$scope.nalog = {};
			})
		console.log($scope.nalog);
	}
	
}]);