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
            '$log','$http',
            function ($log, $http) {
                return {
                    templateUrl: 'partials/plindex.html',
                    restrict: 'A',
                    link: function($scope, element, attrs) {
                        $http({method: 'GET', url: 'data/index.csv.txt'}).
                            success(function(data, status, headers, config) {
                                var rows = 
                                    data.split(/\s*\r*\n+\s*/);
                                var schemas =
                                    rows.filter(function(rowStr) {
                                        return rowStr.trim().length > 0;
                                    }).map(
                                        function(rowStr) {
                                            return rowStr.split(/\s*;\s*/);
                                        }
                                    ).map(
                                        function(rowCols) {
                                            return _.object(['id', 'name'], rowCols);
                                        }
                                    );

                                $scope.schemas = schemas;
                            }).
                            error(function(data, status, headers, config) {
                                $log.warn('fetching index failed', status);
                            });

                        $log.info('the first directive has been activated');
                    }
                }
            }
        ]
    );
