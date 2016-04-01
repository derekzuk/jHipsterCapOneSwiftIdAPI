(function() {
    'use strict';

    angular.module('jhipsterswiftidApp')
        .controller('WebHookController', webHookController);

    webHookController.$inject = ['$http'];
    function webHookController($http) {
        var vm = this;

        var webHookEndpoint = "https://api-sandbox.capitalone.com/oauth/oauth20/token";
        var clientId = "enterpriseapi-sb-q1qyqNUClZs3I2HKGig6LzQq";
        var clientSecret = "c74b56ee5472410478e8c89cf44510a9d07c8d2e";

        vm.registerWebhook = registerWebhook;

        function registerWebhook() {
            console.log('registering webhook');
            $http({
                method: 'POST',
                url: '/api/webhook'
            }, function(success) {
                console.log(success);
            }, function(error) {
                console.log(error);
            });
            }

        function callbackUrl() {
            console.log('in callback-url');
            $http({
                method: 'POST',
                url: '/api/callback-url'
            }, function(success) {
                console.log("success");
                console.log(success);
            }, function(error) {
                console.log("error");
                console.log(error);
            });
            }

        function constructUrl() {
            return webHookEndpoint + "?client_id=" + clientId +
                    "&client_secret=" + clientSecret +
                    "&grant_type=client_credentials";
        }
    }
})();
