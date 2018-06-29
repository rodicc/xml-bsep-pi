(function() {
    angular.module("app")
        .factory("userService", function($http) {
            return {

                login: function(credentials) {
                    return $http.post("/login", credentials);
                }

            }
        })
})();

