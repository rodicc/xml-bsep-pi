angular.module("app")
.controller('Controller', ['$scope', '$rootScope', 'Service', function($scope, $rootScope, Service){
	
	$scope.klijenti = [];
	$scope.racuni = [];
	$scope.firma = {};
	$scope.racun = {};
	$scope.ukidanjeDto = {};
	
	 Service.sviKlijenti()
	 	.then(function(response){
	 		$scope.klijenti = response.data;
	 	});
	 
	 Service.sviRacuni()
	 	.then(function(response){
	 		$scope.racuni = response.data;
	 	});
	
	/* $scope.prikaziRacune = function(index){
		 console.log($scope.klijenti[index].racuni[0].brojRacuna);
	 }*/
	 
	 
	 
	 var racunModal = document.getElementById('racunModal');
	 var dnevnoStanjeModal = document.getElementById('dnevnoStanjeModal');
	 var analitikaModal = document.getElementById('analitikaModal');
	 var ukidanjeModal = document.getElementById('ukidanjeModal');
	 
	 $scope.prikaziRacune = function(index) {
		 	$scope.firma = $scope.klijenti[index];
		 	
		 	console.log($scope.firma.racuni[0].brojRacuna);
		 	racunModal.style.display = "block";
	}
	 
	 $scope.prikaziDnevnoStanje = function(index) {
		 	$scope.racun = $scope.firma.racuni[index];
		 	
		 	console.log($scope.racun);
		 	dnevnoStanjeModal.style.display = "block";
	}
	 
	 $scope.prikaziAnalitikuIzvoda = function(index) {
		 	$scope.dnevnoStanje = $scope.racun.dnevnoStanjeRacuna[index];
		 	
		 	console.log($scope.dnevnoStanje);
		 	analitikaModal.style.display = "block";
	}
	 
	 $scope.ukidanjeRacuna = function(index) {
		 $scope.racunZaUkidanje = $scope.firma.racuni[index];
		 ukidanjeModal.style.display = "block";
	 }
	 
	 $scope.ukiniRacun = function() {
		 var dto = {
				 brojRacunaZaUkidanje : $scope.racunZaUkidanje.brojRacuna,
				 brojRacunaZaPrenosSredstava : $scope.brojRacunaZaPrenos
		 }
		 console.log(dto);
		Service.ukiniRacun(dto)
			.then(function(response){
				
		})
		 
	 }
	 
	 $scope.dodajNoviRacun = function(){
		 var dto = {
				 brojRacuna : $scope.noviRacun,
				 firmaId : $scope.firma.id
		 }
		 Service.noviRacun(dto).then(function(response){
			 
		 })
	 }
	 
	 $scope.dodajNovogKlijenta = function(){
		 Service.noviKlijent($scope.noviKlijent).then(function(response){
			 
		 })
		 $scope.noviKlijent = {};
	 }
	 
	 
	 window.onclick = function(event) {
		    if (event.target == racunModal) {
		        racunModal.style.display = "none";
		    } else  if (event.target == dnevnoStanjeModal) {
		        dnevnoStanjeModal.style.display = "none";
		    } else  if (event.target == analitikaModal) {
		    	analitikaModal.style.display = "none";
		    } else  if (event.target == ukidanjeModal) {
		    	ukidanjeModal.style.display = "none";
		    }
	}
	 
}])