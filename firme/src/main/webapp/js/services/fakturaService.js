angular.module("app")
.factory('fakturaService', function($http){
	return {
		
		posaljiFakturu : function(faktura){
			console.log("servis");
			return $http.post("/fakture", faktura).catch(angular.noop);
		}
		
		
	}
})