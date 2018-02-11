angular.module("app")
.factory('izvodService', function($http){
	return {
		
		posaljiNalogZaIzvod : function(izvod){
			return $http.post("/izvodi", izvod).catch(angular.noop);
		}
	}
})