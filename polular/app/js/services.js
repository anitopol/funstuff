'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.
angular.module(
        'myApp.services', []
    ).service(
        'utils',
        ['$q', '$http', '$log', function ($q, $http, $log) {
            return {
                l: function() {
                    var argsArr = _.map(arguments, function(i) {return i;});

                    // console.log.apply(console, argsArr);
                    $log.warn.apply($log, argsArr);

                    return _.last(arguments);
                },
                strLCS: function (a, b) {
                    var m = a.length, n = b.length, C = [], i, j;
                    for (i = 0; i <= m; i++) {
                        C.push([0]);
                    }
                    for (j = 0; j < n; j++) {
                        C[0].push(0);
                    }
                    for (i = 0; i < m; i++) {
                        for (j = 0; j < n; j++) {
                            C[i + 1][j + 1] = a[i] === b[j] ? C[i][j] + 1 : Math.max(C[i + 1][j], C[i][j + 1]);
                        }
                    }
                    return (function bt(i, j) {
                        if (i * j === 0) {
                            return "";
                        }
                        if (a[i - 1] === b[j - 1]) {
                            return bt(i - 1, j - 1) + a[i - 1];
                        }
                        return (C[i][j - 1] > C[i - 1][j]) ? bt(i, j - 1) : bt(i - 1, j);
                    }(m, n));
                },
                editDist: function (a, b) {
                    return a.length + b.length - 2 * this.strLCS(a, b).length;
                },
                strLCP: function(a, b) {
                    var m = a.length, n = b.length;
                    var c = m < n ? m : n;
                    for (var i = 0; i < c; i++) {
                      if (a.charAt(i) != b.charAt(i)) {
                          return i;
                      }  
                    }
                    return c;
                },
                tsvPromise: function (filePath, rowToObjFun) {
                    var deferred = $q.defer();

                    $http(
                        {method: 'GET', url: filePath + '?r='+_.random(1024*1024*1024)}
                    ).success(
                        function (data, status, headers, config) {
                            var rows =
                                data.split(/\s*\r*\n+\s*/);

                            var cells =
                                rows.filter(function (rowStr) {
                                    return rowStr.trim().length > 0 && rowStr.trim().charAt(0) != '#';
                                }).map(
                                    function (rowStr) {
                                        return rowStr.split(/\s*[;\t]\s*/);
                                    }
                                );

                            if (_.isUndefined(rowToObjFun)) {
                                deferred.resolve(cells);
                            } else {
                                deferred.resolve(cells.map(rowToObjFun));
                            }
                        }
                    ).error(
                        function (data, status, headers, config) {
                            var statusMsg = 'fetching ' + filePath + ' failed: ' + status;
                            $log.warn(statusMsg);
                            deferred.reject(statusMsg);
                        }
                    );

                    return deferred.promise;
                }
            };
        }]
    ).factory(
        'data',
        ['$q', '$http', '$log', 'utils', function ($q, $http, $log, utils) {
            var schemaListPromise =
                utils.tsvPromise(
                    'data/index.csv.txt',
                    function (rowCols) {
                        //noinspection ConstantIfStatementJS
                        if (false) {
                            alert(
                                {
                                    id: '', name: ''
                                }
                            );
                        }
                        return _.object(['id', 'name'], rowCols);
                    }
                );

            var schemaMapPromise =
                schemaListPromise.then(
                    function (schemaList) {
                        return $q.all(
                            schemaList.map(
                                function (schemaEntry) {
                                    return utils.tsvPromise(
                                        'data/' + schemaEntry.id + '--schema.csv.txt',
                                        function (rowCols) {
                                            //noinspection ConstantIfStatementJS
                                            if (false) {
                                                alert(
                                                    {
                                                        id: '', name: '', columnType: '', cellClassShort: '', width: ''
                                                    }
                                                );
                                            }
                                            return _.object(['id', 'name', 'columnType', 'cellClassShort', 'width'], rowCols);
                                        }
                                    ).then(
                                        function (columns) {
                                            return utils.tsvPromise(
                                                    'data/' + schemaEntry.id + '.csv.txt'
                                                ).then(
                                                function (data) {
                                                    var schemaClone = _.clone(schemaEntry);
                                                    schemaClone['columns'] = columns;
                                                    schemaClone['data'] = data;
                                                    return schemaClone;
                                                }
                                            );
                                        }
                                    );
                                }
                            )
                        );
                    }
                ).then(
                    function (fullSchemas) {
                        return _.indexBy(
                            fullSchemas,
                            function (fullSchema) {
                                return fullSchema.id;
                            }
                        );
                    }
                );

            return {
                schemaListPromise: schemaListPromise,
                schemaMapPromise: schemaMapPromise
            };
        }]
    ).value(
        'version',
        '0.1'
    );
