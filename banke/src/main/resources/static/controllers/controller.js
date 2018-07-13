angular.module("app")
.controller('Controller', ['$scope', '$rootScope', 'Service', function($scope, $rootScope, Service){
	
	$scope.klijenti = [];
	$scope.racuni = [];
	
	$scope.firma = {};
	$scope.racun = {};
	$scope.ukidanjeDto = {};
	$scope.racunZaUkidanje = {};
	$scope.ukinutiRacuni = [];
	
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

	 $scope.getUkinutiRacuni = function(){
		 Service.sviUkinutiRacuni()
	 
	 	.then(function(response){
	 		$scope.ukinutiRacuni = response.data;
	 	});
	 }; $scope.getUkinutiRacuni();
	 
	 var racunModal = document.getElementById('racunModal');
	 var dnevnoStanjeModal = document.getElementById('dnevnoStanjeModal');
	 var analitikaModal = document.getElementById('analitikaModal');
	 var ukidanjeModal = document.getElementById('ukidanjeModal');
	 
	 $scope.prikaziRacune = function(index) {
		 	$scope.firma = $scope.klijenti[index];
		 	$scope.firma.indexAtParent = index;
		 	racunModal.style.display = "block";
	}
	 
	 $scope.prikaziDnevnoStanje = function(index) {
		 	if($scope.firma.racuni != undefined){
		 		$scope.racun = $scope.firma.racuni[index];
		 	} else {
		 		$scope.racun = $scope.racuni[index];
		 	}
		 	$scope.racun.indexAtParent = index;
		 	dnevnoStanjeModal.style.display = "block";
	}
	 
	 $scope.prikaziAnalitikuIzvoda = function(index) {
		 	$scope.dnevnoStanje = $scope.racun.dnevnoStanjeRacuna[index];
		 	$scope.dnevnoStanje.indexAtParent = index;
		 	analitikaModal.style.display = "block";
	}
	 
	 $scope.ukidanjeRacuna = function(index) {
		 if($scope.firma.racuni != undefined){
			 $scope.racunZaUkidanje = $scope.firma.racuni[index];
		 } else {
			 $scope.racunZaUkidanje = $scope.racuni[index];
		 }
		 $scope.racunZaUkidanje.indexAtParent = index;
		 ukidanjeModal.style.display = "block";
	 }
	 
	 $scope.ukiniRacun = function(index) {
		 var dto = {
				 brojRacunaZaUkidanje : $scope.racunZaUkidanje.brojRacuna,
				 brojRacunaZaPrenosSredstava : $scope.brojRacunaZaPrenos
		 }
		Service.ukiniRacun(dto)
			.then(function(response){
			$scope.firma.racuni[index].vazeci = false;
			ukidanjeModal.style.display = "none";
		})
		$scope.racunZaUkidanje = {};
		$scope.brojRacunaZaPrenos = "";
		 
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
	 
	 $scope.generisiIzvestajBanke = function(){
		 Service.izvestajBanke($scope.brRacunaBanke).then(function(response){
			console.log("izvestaj banke");
		 })
		 $scope.brRacunaBanke = "";
	 }
	 
	 $scope.generisiIzvestajKlijenta = function(){
		 Service.izvestajKlijenta($scope.izvodKlijenta).then(function(response){
			console.log("izvestaj klijenta");
		 })
		 $scope.izvodKlijenta = {};
	 }
	 
	 window.onclick = function(event) {
		    if (event.target == racunModal) {
		        racunModal.style.display = "none";
		        $scope.firma = {};
		    } else  if (event.target == dnevnoStanjeModal) {
		        dnevnoStanjeModal.style.display = "none";
		        $scope.racun = {};
		    } else  if (event.target == analitikaModal) {
		    	analitikaModal.style.display = "none";
		    	$scope.dnevnoStanje = {};
		    } else  if (event.target == ukidanjeModal) {
		    	ukidanjeModal.style.display = "none";
		    }
	}
	 
}])