angular.module("app").factory(
		'certificateService',
		function($http) {

			return {
				generateSelfSigned : function(certificate) {
					return $http.post('/certificates/newSelfSigned', certificate);
				},
				
				sendCSR : function(certificate) {
					return $http.post('/certificates/newCSR/'
							+ certificate.issuer, certificate);
				},

				getAllCACertificates : function() {
					return $http.get('/certificates');
				},

				getCertificateFile : function(serialNumber, caAlias) {
				return $http.post('/certificates/download/' + caAlias, serialNumber);

				},

				checkCertificate : function(serialNumber, caAlias) {
					return $http.post('/certificates/check/' + caAlias,
							serialNumber);
				},

				revokeCertificate : function(serialNumber, caAlias) {
					var req = {
						method : 'POST',
						url : '/certificates/revoke/' + caAlias,
						headers : {
							'Content-Type' : 'text/plain'
						},
						data : serialNumber
					}

					return $http(req);
				}
			};
		})
