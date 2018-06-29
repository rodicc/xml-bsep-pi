(function() {
    angular.module('app')
        .controller("userController", function($scope, $rootScope, $state, userService) {
            $scope.messages = {
                failedLogin: ""
            }
            $scope.user = {};

            $scope.login = function() {
                userService.login($scope.credentials)
                    .then(function(response) {    
                        var user = response.headers("User");
                        $rootScope.user = user;
                        localStorage.setItem("User", user);
                        
                        $rootScope.role = response.headers("Role");
                        localStorage.setItem("Role", $rootScope.role);

                        var token = response.headers("Authorization");
                        localStorage.setItem("Authorization", token);

                        $state.go("sertifikat");
                    }, function(error) {
                        $scope.messages.failedLogin = "Incorrect username or password";
                    })
            };

            $scope.logout = function() {
                localStorage.removeItem("User");
                localStorage.removeItem("Role");
                localStorage.removeItem("Authorization");
                $rootScope.user = null;
                $rootScope.role = null;
                $state.go("login");
            };

        });
})();