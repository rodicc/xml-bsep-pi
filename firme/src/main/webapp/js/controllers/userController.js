(function() {
    angular.module('app')
        .controller("userController", function($scope, $rootScope, $state, userService) {
            $scope.messages = {
                successfulRegistration: "",
                failedRegistration: "",
                failedLogin: ""
            }

            $scope.user = {};

            $scope.login = function() {
                userService.login($scope.credentials)
                    .then(function(response) {    
                        var user = response.headers("User");
                        $rootScope.user = user;
                        localStorage.setItem("User", user);

                        $state.go("faktura");
                    }, function(error) {
                        $scope.messages.failedLogin = "Incorrect username or password";
                    })
            };

            $scope.register = function() {
                userService.register($scope.user)
                    .then(function(response) {
                        $scope.messages.failedRegistration = "";
                        $scope.messages.successfulRegistration = response.data;
                    }, function(error) {
                        $scope.messages.successfulRegistration = "";
                        $scope.messages.failedRegistration =  error.data.errors[0];
                    });
            };

            $scope.logout = function() {
                localStorage.removeItem("User");
                $rootScope.user = null;
                userService.logout()
                    .then(function(response) {
                        $state.go("login");
                    });
            };

        });
})();