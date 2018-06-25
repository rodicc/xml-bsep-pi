angular.module("app")
.controller('certificateController', ['$scope', 'certificateService', function($scope, certificateService){
	
	$scope.CACertificates = [];
	
	
	certificateService.getAllCACertificates()
		.then(function(response){
			$scope.CACertificates = response.data;
		})
	
	$scope.sendCSR = function(certificate){
		console.log("CSR")
		certificateService.sendCSR(certificate)
			.then(function(response){
				console.log(response);
			})
	}
	
	$scope.submitCertificate = function(certificate){
		
		certificateService.submitCertificate(certificate)
			.then(function(response){
				console.log(response);
			})
	}
	
	$scope.revokeCertificate = function(revokeCertificateDto){
		certificateService.revokeCertificate(revokeCertificateDto)
			.then(function(response){
				console.log(response);
			})
	}
	
	$scope.checkCertificate = function(checkAlias){
		certificateService.checkCertificate(checkAlias)
			.then(function(response){
				alert(response.data);
			})
	}
	
	$scope.getCertificate = function(alias){
		certificateService.getCertificate(alias)
			.then(function(response){
				alert(response.data.certificateText);
			})
	}
	
	$scope.getCertificateFile = function(alias){
		certificateService.getCertificateFile(alias)
			.then(function(response){
				console.log(response);
				download(alias+".cer", response.data);
			}, function(error) {
				alert("Certificate with Serial Number: "+ alias + " could not be found.");
			})
		
			
	}
	
	var download = function(filename, text) {
	    var element = document.createElement('a');
	    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
	    element.setAttribute('download', filename);

	    element.style.display = 'none';
	    document.body.appendChild(element);

	    element.click();

	    document.body.removeChild(element);
	}
	
	
}])