angular.module('app')
.controller('nalogController', ['$scope','nalogService', '$state', function($scope, nalogService, $state) {
	
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
			}, function(error) {
				$scope.obavestenja.validan = false;
				$scope.obavestenja.nevalidan = true;
				if (error.status == 401 || error.status == 403) {
					$state.go("login");
				}
			})
		console.log($scope.nalog);
	}
	
}]);