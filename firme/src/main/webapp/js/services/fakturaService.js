angular.module("app")
.factory('fakturaService', function($http){
	return {
		
		posaljiFakturu : function(faktura){
			return $http.post("/fakture", faktura);
		},
		
		prikaziSveFakture : function(){
			return $http.get("/fakture");
		}
		
	}
})