angular.module("app")
.factory('redirectService', function($q, $rootScope, $location, $state){
	return{
		
		authenticated : function () {
		      var deferred = $q.defer();
		      if ($rootScope.user == null) {
		    	  //$location.path("/login");
		    	  $state.go("login")
		          deferred.reject();
		    	  
		      } else {
		          deferred.resolve();
		      }
		      return deferred.promise;
		}	
	}
})