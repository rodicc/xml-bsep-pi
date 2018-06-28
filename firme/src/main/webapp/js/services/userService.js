(function() {
    angular.module("app")
        .factory("userService", function($http) {
            return {

                login: function(credentials) {
                    return $http.post("/login", credentials);
                },

                register: function(userData) {
                    return $http.post("/register", userData);
                },

                logout: function() {
                    return $http.get("/userlogout");
                }

            }
        })
})();

