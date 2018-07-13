angular.module("app")
.factory('Service', function($http){
	return {
		sviRacuni : function(){
			return $http.get("/racuni");
		},
		
		sviUkinutiRacuni : function(){
			return $http.get("/racuni/ukinuti");
		},
		
		ukiniRacun : function(dto){
			return $http.post("/racuni/ukini", dto);
		},
		
		noviRacun : function(dto){
			return $http.post("/racuni/novi", dto);
		},
		
		sviKlijenti : function(){
			return $http.get("/klijenti");
		},
		
		noviKlijent : function(dto){
			return $http.post("/klijenti/novi", dto);
		},
		
		izvestajBanke : function(brRacuna){
			return $http.post("/izvestaj/spisakRacuna", brRacuna);
		},
		
		izvestajKlijenta : function(dto){
			return $http.post("/izvestaj/izvodKlijenta", dto);
		}
		
		
	}
})