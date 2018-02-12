angular.module("app")
.factory('porukeService', function($http){
	return {
		
		prikaziSvePoruke : function(){
			return $http.get("/poruke").catch(angular.noop);
		}
		
	}
})