angular.module("app")
.factory('Service', function($http){
	return {
		sviRacuni : function(){
			return $http.get("/racuni");
		},
		
		sviUkinutiRacuni : function(){
			return $http.get("/racuni/ukinuti");
		}
		
		
	}
})