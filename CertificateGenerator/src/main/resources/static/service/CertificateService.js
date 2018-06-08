angular.module("app")
.factory('certificateService', function($http){
	
	return {
		submitCertificate : function(certificate){
			return $http.post('/certificate', certificate);
		}
	}
	
	
})