angular.module('app')
.controller('fakturaController', ['$scope', 'fakturaService', '$state', function($scope, fakturaService, $state){
	
	$scope.stavke = [];	
	$scope.stavkeSize = 0;	
	$scope.vrednostRobe = 0;	
	$scope.ukupanRabat = 0;	
	$scope.iznosZaUplatu = 0;
	$scope.selektovanoPreduzece = [];	
	$scope.selektovanaStavka = [];
	$scope.faktura = {};
	
	
	$scope.dodajStavku = function(){
		
		var stavkaZaDodavanje = {	
				redniBroj : $scope.stavke.length + 1,
				naziv : $scope.naziv, 
				kolicina : $scope.kolicina, 
				jedinicnaCena: $scope.jedinicnaCena,
				jedinicaMere : $scope.jedinicaMere,
				vrednost: Math.round($scope.kolicina * $scope.jedinicnaCena * 100)/100,
				rabat: $scope.rabat,
				iznosRabata : Math.round($scope.kolicina * $scope.jedinicnaCena * $scope.rabat)/100 ,
				umanjenoZaRabat : $scope.jedinicnaCena - ($scope.jedinicnaCena * $scope.rabat/100),
				ukupno : $scope.kolicina * $scope.jedinicnaCena -  $scope.kolicina * $scope.jedinicnaCena * $scope.rabat / 100
		};
		
		$scope.stavke.push(stavkaZaDodavanje);
		$scope.stavkeSize = $scope.stavke.length;
		
		$scope.vrednostRobe = Math.round(($scope.vrednostRobe + stavkaZaDodavanje.ukupno)*100)/100;
		$scope.ukupanRabat = Math.round(($scope.ukupanRabat + stavkaZaDodavanje.iznosRabata)*100)/100;
		$scope.iznosZaUplatu = Math.round(($scope.iznosZaUplatu + stavkaZaDodavanje.ukupno)*100)/100;
		
	}
	
	$scope.posaljiFakturu = function() {

		$scope.faktura.vrednostRobe = $scope.vrednostRobe;
		$scope.faktura.ukupanRabat = $scope.ukupanRabat;
		$scope.faktura.iznosZaUplatu = $scope.iznosZaUplatu;
		$scope.faktura.stavkeFakture = $scope.stavke;
		
		fakturaService.posaljiFakturu($scope.faktura)
			.then(function(response) {

			}, function(error) {
				if (error.status == 401 || error.status == 403) {
					$state.go("login");
				}
			});
	}
	
}])

.controller('pregledFakturaController', ['$scope', 'fakturaService', '$state', function($scope, fakturaService, $state){
	
	$scope.fakture = {};
	
	fakturaService.prikaziSveFakture()
		.then(function(response) {
			$scope.fakture = response.data;
		}, function(error) {
			if (error.status == 401 || error.status == 403) {
				$state.go("login");
			}
		})
}])


;