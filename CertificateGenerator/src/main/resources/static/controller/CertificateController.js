angular.module("app")
.controller('certificateController', ['$scope', 'certificateService', function($scope, certificateService){
	
	$scope.submitCertificate = function(certificate){
		certificateService.submitCertificate(certificate);
	}
	
}])