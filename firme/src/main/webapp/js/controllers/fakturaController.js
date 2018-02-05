angular.module('app')
.controller('fakturaController', ['$scope', function($scope){
	
	$scope.stavke = [];
	
	$scope.stavkeSize = 0;
	
	$scope.vrednostRobe = 0;
	
	$scope.ukupnoRabat = 0;
	
	$scope.iznosZaUplatu = 0;
	
	$scope.selektovanoPreduzece = [];
	
	$scope.selektovanaStavka = [];
	
	
	$scope.dodajStavku = function(){
		
		var stavkaZaDodavanje = {	
				naziv : $scope.naziv, 
				kolicina : $scope.kolicina, 
				jedinicnaCena: $scope.jedinicnaCena,
				jedinicaMere : $scope.jedinicaMere,
				vrednost: Math.round($scope.kolicina * $scope.jedinicnaCena * 100)/100,
				rabat: $scope.rabat,
				iznosRabata : Math.round($scope.kolicina * $scope.jedinicnaCena * $scope.rabat)/100 ,
				umanjenoZaRabat : $scope.jedinicnaCena - ($scope.kolicina * $scope.jedinicnaCena * $scope.rabat/100),
				ukupno : $scope.kolicina * $scope.jedinicnaCena -  $scope.kolicina * $scope.jedinicnaCena * $scope.rabat / 100
		};
		
		$scope.stavke.push(stavkaZaDodavanje);
		
		$scope.vrednostRobe = $scope.vrednostRobe + stavkaZaDodavanje.ukupno;
		$scope.ukupnoRabat = $scope.ukupnoRabat + stavkaZaDodavanje.iznosRabata;
		$scope.iznosZaUplatu = $scope.iznosZaUplatu + stavkaZaDodavanje.ukupno;
		$scope.stavkeSize = $scope.stavke.length;
	}
	
	
	
}]);