angular.module("app")
.controller('certificateController', ['$scope', 'certificateService', function($scope, certificateService){
	
	$scope.CACertificates = [];
	$scope.issuer = '';
	
	certificateService.getAllCACertificates()
		.then(function(response){
			$scope.CACertificates = response.data;
		})
	
	$scope.sendCSR = function(certificate){
		console.log("CSR")
		certificateService.sendCSR(certificate, certificate.issuer)
			.then(function(response){
				alert("S/N: "+response.data);
				console.log(response.data);
			})
	}
	
	
	$scope.revokeCertificate = function(serialNumber){
		certificateService.revokeCertificate(serialNumber, $scope.issuer)
			.then(function(response){
				alert(response.data);
			})
	}
	
	$scope.checkCertificate = function(serialNumber){
		certificateService.checkCertificate(serialNumber, $scope.issuer)
			.then(function(response){
				alert(response.data);
			})
	}
	
	
	$scope.getCertificateFile = function(serialNumber){
		certificateService.getCertificateFile(serialNumber, $scope.issuer)
			.then(function(response){
				console.log(response);
				download(serialNumber+".cer", response.data);
			}, function(error) {
				alert("Certificate with Serial Number: "+ serialNumber + " could not be found.");
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