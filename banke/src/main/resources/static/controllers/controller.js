angular.module("app")
.controller('Controller', ['$scope', '$rootScope', 'Service', function($scope, $rootScope, Service){
	
	$scope.klijenti = [];
	$scope.racuni = [];
	$scope.firma = {};
	$scope.racun = {};
	$scope.ukidanjeDto = {};
	$scope.racunZaUkidanje = {};
	
	 $scope.getKlijenti = function(){
		 Service.sviKlijenti()
	
	 	.then(function(response){
	 		$scope.klijenti = response.data;
	 	});
	 }; $scope.getKlijenti();
	 
	 $scope.getRacuni = function(){
		 Service.sviRacuni()
	 
	 	.then(function(response){
	 		$scope.racuni = response.data;
	 	});
	 }; $scope.getRacuni();

	 
	 var racunModal = document.getElementById('racunModal');
	 var dnevnoStanjeModal = document.getElementById('dnevnoStanjeModal');
	 var analitikaModal = document.getElementById('analitikaModal');
	 var ukidanjeModal = document.getElementById('ukidanjeModal');
	 
	 $scope.prikaziRacune = function(index) {
		 	$scope.firma = $scope.klijenti[index];
		 	
		 	racunModal.style.display = "block";
	}
	 
	 $scope.prikaziDnevnoStanje = function(index) {
		 	$scope.racun = $scope.firma.racuni[index];
		 	
		 	dnevnoStanjeModal.style.display = "block";
	}
	 
	 $scope.prikaziAnalitikuIzvoda = function(index) {
		 	$scope.dnevnoStanje = $scope.racun.dnevnoStanjeRacuna[index];
		 	
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
		Service.ukiniRacun(dto)
			.then(function(response){
			ukidanjeModal.style.display = "none";
			for(var i = 0; i < $scope.firma.racuni.lenght; i++){
				if($scope.firma.racuni[i].brojRacuna == $scope.racunZaUkidanje.brojRacuna){
					$scope.firma.racuni.splice(i,1);
				}
			}
		})
		$scope.racunZaUkidanje = {};
		 
	 }
	 
	 $scope.dodajNoviRacun = function(){
		 var dto = {
				 brojRacuna : $scope.noviRacun,
				 firmaId : $scope.firma.id
		 }
		 Service.noviRacun(dto).then(function(response){
			 $scope.racuni.push(response.data);
			 $scope.firma.racuni.push(response.data);
		 })
		 $scope.noviRacun = "";
	 }
	 
	 $scope.dodajNovogKlijenta = function(){
		 Service.noviKlijent($scope.noviKlijent).then(function(response){
			 $scope.klijenti.push(response.data);
			 $scope.racuni.push(response.data.racuni);
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