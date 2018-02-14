angular.module("app")
.factory('nalogService', function($http){
	return{
		
		posaljiNalog : function(nalog){
			return $http.post("/nalozi", nalog);
		}
		
	}
})

