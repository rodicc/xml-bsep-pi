(function() {
    angular.module('app')
        .controller("userUpdateController", function($scope, userService) {
            $scope.messages = {
                failedUpdate: "",
                successfulUpdate: ""
            }

            $scope.user = {};

            $scope.updatePassword = function() {
                userService.updatePassword($scope.user)
                    .then(function(response) {
                        $scope.messages.failedUpdate = "";
                        $scope.messages.successfulUpdate = response.data.message;
                    }, function(error) {
                        $scope.messages.successfulUpdate = "";
                        $scope.messages.failedUpdate = error.data.message;
                        
                    }) 
            }
        });
})();