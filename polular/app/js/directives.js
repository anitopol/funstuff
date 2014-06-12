'use strict';

angular.module('myApp.directives', []).directive(
        'appVersion',
        [
            'version',
            function (version) {
                return function (scope, elm, attrs) {
                    elm.text(version);
                };
            }
        ]
    ).directive(
        'plindex',
        [
            '$log','$http', 'utils', 'data',
            function ($log, $http, utils, data) {
                return {
                    templateUrl: 'partials/plindex.html',
                    restrict: 'A',
                    link: function($scope, element, attrs) {
                        data.schemaListPromise.then(function(schemas) {
                            $scope.schemas = schemas
                        });
                    }
                }
            }
        ]
    );
