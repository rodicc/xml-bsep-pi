angular.module("app")
.factory('fakturaService', function($http){
	return {
		
		posaljiFakturu : function(faktura){
			console.log("servis");
			return $http.post("/fakture", faktura).catch(angular.noop);
		},
		
		prikaziSveFakture : function(){
			return $http.get("/fakture").catch(angular.noop);
		}
		
	}
})