'use strict';

/* Controllers */

angular.module(
        'myApp.controllers', []
    ).controller(
        'SchemaTableController',
        [
            '$scope', '$routeParams', '$log', '$q', '$filter', 'ngTableParams', 'utils', 'data', 
            function ($scope, $routeParams, $log, $q, $filter, ngTableParams, utils, data) {
                $scope.rowCollection = [ ];
                $scope.columnCollection = [ ];
                $scope.ngTableParams = new ngTableParams(
                    {
                        page: 1,
                        count: 20,
                        filter: { }
                    },
                    {
                        counts: [10, 20, 30, 50, 100],
                        total: function() { return $scope.rowCollection.length; }, 
                        getData: function($defer, params) {
                            var filteredData = params.filter() ?
                                $filter('filter')($scope.rowCollection, params.filter()) :
                                $scope.rowCollection;
                            var orderedData = params.sorting() ?
                                $filter('orderBy')(filteredData, params.orderBy()) :
                                filteredData;
                            
                            params.total(orderedData.length);
                            $defer.resolve(
                                orderedData.slice(
                                    (params.page() - 1) * params.count(), 
                                    params.page() * params.count()
                                )
                            );
                        }
                    }
                );
    
                $scope.quizQueue = [ ];
                $scope.quizLog = [ ];
                $scope.quizIdx = 0;
                $scope.quizCurrent = function(){
                    return $scope.quizQueue[$scope.quizIdx];
                };
                $scope.changeSelection = function(row) {
                    $scope.selectedRows = _.filter(
                        $scope.rowCollection,
                        function(row) { return row.$selected; }
                    );
                };
                $scope.selectedRows = [ ];
                $scope.startQuiz = function() { $log.warn('$scope.startQuiz() not valid to be called'); };
                $scope.nextInput = function() { $log.warn('$scope.nextInput() not valid to be called'); };
    
                data.schemaMapPromise.then(
                    function (schemaMap) {
                        var schema = schemaMap[$routeParams.schemaId]; // TODO: recover from injecting rogue values 

                        $scope.schema = schema;

                        var idCol = _.find(
                            schema.columns,
                            function(col) {
                                return col.columnType == 'id';
                            }
                        );

                        var inputCols =
                            _.filter(
                                schema.columns,
                                function (col) {
                                    return col.columnType == 'input';
                                }
                            );

                        var lcsLevels = [0, 1, 2, 4];
                        function statsColumnKey(idx) { return 'stats' + idx; }
                        var statsColumnKeys = _(lcsLevels).map(
                            function(val, idx) { return statsColumnKey(idx); }
                        );

                        var emptyRowStats = function() {
                            return _.object(
                                _.map(
                                    inputCols,
                                    function(col) {
                                        return [col.id, _.map(lcsLevels, function () { return 0; })]
                                    }
                                )
                            );
                        };

                        var schemaStatsJson = window.localStorage[$routeParams.schemaId];
                        var schemaStats = angular.fromJson(schemaStatsJson ? schemaStatsJson : {});
                        function statsMapForRow(row) {
                            if (
                                !_.has(schemaStats, row[idCol.id]) ||
                                    !_.every(
                                        inputCols,
                                        function (col) {
                                            var stats = schemaStats[row[idCol.id]];
                                            return _.has(stats, col.id) &&
                                                _.isArray(stats[col.id]) &&
                                                lcsLevels.length == stats[col.id].length;
                                        }
                                    )
                                ) {
                                schemaStats[row[idCol.id]] = emptyRowStats();
                            }

                            return schemaStats[row[idCol.id]];
                        }
                        function statsForRow(row) {
                            return _.reduce(
                                _.pluck(
                                    _.pairs(
                                        statsMapForRow(row)
                                    ),
                                    "1"
                                ),
                                function(res, cur) {
                                    return _.map(
                                        _.zip(res, cur),
                                        function(arr) { return _.min(arr); }
                                    );
                                }
                            );
                        }
                        function statsForCol(row, inputCol) {
                            return statsMapForRow(row)[inputCol.id];
                        }

                        var columnKeys = schema.columns.map(
                            function(col) { return col.id; }
                        );
    
                        $scope.rowCollection =
                            schema.data.map(function (row) {
                                var rowObj = _.object(columnKeys, row);
                                return _.extend(
                                    rowObj,
                                    _.object(statsColumnKeys, statsForRow(rowObj))
                                );
                            });

                        function isColumnGreedy(col) {
                            return col.width.indexOf("+") >= 0;
                        }
                        var greedyColumnCount = 
                            schema.columns.filter(isColumnGreedy).length;

                        $scope.columnCollection = 
                            lcsLevels.map(
                                function(level, index) {
                                    return {
                                        visible: true,
                                        field: statsColumnKey(index),
                                        sortable: statsColumnKey(index),
                                        title: '' + level,
                                        filter: _.object([statsColumnKey(index)], ['text']),
                                        cellClass: 'schemaTable_category',
                                        colStyle: {
                                            'max-width': '1.5em',
                                            'width': '1.5em'
                                        }
                                    }
                                }
                            ).concat(
                                schema.columns.map(
                                    function(col) {
                                        var cellClasses = col.cellClassShort.split(/ +/);
                                        cellClasses.push(col.columnType);
                                        var colDef = {
                                            visible: true,
                                            field: col.id,
                                            sortable: col.id,
                                            title: col.name,
                                            filter: _.object([col.id], ['text']),
                                            cellClass: cellClasses.map(function(cc) { return 'schemaTable_' + cc;}).join(' ')/*,
                                             groupable: col.columnType == 'category',
                                             */
                                        };

                                        var colStyleObj = {};
                                        if (isColumnGreedy(col)) {
                                            colStyleObj = {
                                                'min-width': '' + parseInt(col.width) + 'px',
                                                'width': Math.ceil(100.0 / greedyColumnCount) + "%"
                                            };
                                        } else {
                                            colStyleObj = {
                                                'max-width': '' + parseInt(col.width) + 'px',
                                                'width': '' + parseInt(col.width) + 'px'
                                            };
                                        }
                                        colDef.colStyle = colStyleObj;
                                        return colDef;
                                    }
                                )
                            );

                        $scope.ngTableParams.sorting(idCol.id, 'asc');
                        $scope.ngTableParams.reload();
                        
                        $scope.startQuiz = function() {
                            var selectedData = 
                                _.flatten(
                                    _.map(
                                        $scope.selectedRows,
                                        function(row) {
                                            var first = inputCols[0];
                                            var lcpLen = 
                                                _.min(
                                                    _.map(
                                                        _.rest(inputCols),
                                                        function(col) {
                                                            return utils.strLCP(row[first.id], row[col.id]);
                                                        }
                                                    )
                                                );
                                            return _.map(
                                                inputCols,
                                                function (col) {
                                                    return { 
                                                        id: row[idCol.id], 
                                                        inputCol: col, 
                                                        expected: row[col.id],
                                                        actual: row[col.id].substring(0, _.random(_.max([0, lcpLen-1]))),
                                                        stats: statsForCol(row, col),
                                                        editDist: function() {
                                                            return utils.editDist(this.actual, this.expected);
                                                        },
                                                        editDistCss: function() {
                                                            var editDistVal = this.editDist();
                                                            return _.reduce(
                                                                [1, 2, 3, 5, 8, 13],
                                                                function (memo, elem) {
                                                                    return editDistVal >= elem ? memo + 1 : memo;
                                                                },
                                                                0
                                                            );
                                                        }
                                                    };
                                                }
                                            );
                                        }
                                    )
                                );
    
                            $scope.quizLog = []; 
                            $scope.quizQueue = selectedData;
                            $scope.quizIdx = _.random(0, selectedData.length - 1);
                            
                            $scope.nextInput = function() {
                                var quizCurrent = $scope.quizCurrent();
                                var quizCurrentDist = quizCurrent.editDist();
                                quizCurrent.stats = _(quizCurrent.stats).map(
                                    function(count, idx) {
                                        if (lcsLevels[idx] >= quizCurrentDist) {
                                            return count + 1;
                                        }
                                        return 0;
                                    }
                                );
                                $scope.quizLog.push(quizCurrent);
                                $scope.quizQueue.splice($scope.quizIdx, 1);
                                $scope.quizIdx = _.random(0, $scope.quizQueue.length - 1);
                                
                                if ($scope.quizQueue.length == 0) {
                                    _($scope.quizLog).each(
                                        function(quiz) {
                                            schemaStats[quiz.id][quiz.inputCol.id] = quiz.stats;
                                        }
                                    );
                                    //  re-set all relevant fields into tabular data
                                    _.chain($scope.quizLog).map(
                                        function(quiz) { return quiz.id; }
                                    ).uniq().each(
                                        function(rowId) {
                                            _.chain($scope.rowCollection).filter(
                                                function(row) { return row[idCol.id] == rowId; }
                                            ).each(
                                                function(row){
                                                    _.extend(
                                                        row,
                                                        _.object(statsColumnKeys, statsForRow(row))
                                                    )
                                                }
                                            );
                                        }
                                    );

                                    window.localStorage[$routeParams.schemaId] = angular.toJson(schemaStats);
                                }
                            }
                        };
                    }
                );
            }
        ]
    );
