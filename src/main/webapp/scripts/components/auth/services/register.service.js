'use strict';

angular.module('jhipsterswiftidApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


