(function() {
    'use strict';

    angular.module('jhipsterswiftidApp')
        .directive('capitalOneWebHook', capitalOneWebHook);

    function capitalOneWebHook() {
        var directive = {
            restrict: 'E',
            templateUrl: 'scripts/components/webhook/webhook.html',
            controller: 'WebHookController',
            controllerAs: 'vm'
        };

        return directive;
    }
})();
