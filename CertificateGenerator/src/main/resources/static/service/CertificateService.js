angular.module("app")
.factory('certificateService', function($http){
	
	return {
		sendCSR : function(certificate){
			return $http.post('/certificates/newCSR/' + certificate.issuer, certificate);
		},
		submitCertificate : function(certificate){
			return $http.post('/certificates/new', certificate);
		},
		
		getAllCACertificates : function(){
			return $http.get('/certificates');
		},
		
		getCertificate : function(alias){
			return $http.get('/certificates/' + alias);
		},
		
		getCertificateFile : function(alias){
			//return $http.get('/certificates/file/' + alias);
			
		return	$http({method: 'GET', url: '/certificates/file/' + alias}).
			  success(function(data, status, headers, config) {
			     var anchor = angular.element('<a/>');
			     anchor.attr({
			         href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
			         target: '_blank',
			         download: 'certificate.cer'
			     })[0].click();

			  }).
			  error(function(data, status, headers, config) {
			    // handle error
			  });
		},
		
		
		
		
		
		revokeCertificate : function(revokeCertificateDto){
			return $http.post('/certificates/revoke', revokeCertificateDto);
		},
		
		checkCertificate : function(checkAlias){
			return $http.get('/certificates/check/' + checkAlias);
		}
	}
	
	
})