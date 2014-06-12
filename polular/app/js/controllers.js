'use strict';

/* Controllers */

angular.module(
        'myApp.controllers', []
    ).controller(
        'SchemaTableController',
        ['$scope', '$routeParams', '$log', '$q', 'utils', 'data', function ($scope, $routeParams, $log, $q, utils, data) {
            $scope.quizQueue = [ ];
            $scope.quizLog = [ ];
            $scope.quizIdx = 0;
            $scope.quizCurrent = function(){
                return $scope.quizQueue[$scope.quizIdx];
            };
            $scope.selectedRows = [ ];
            $scope.dynamicData = [ ];
            $scope.dynamicColumns = [ ];
            $scope.gridOptions = { 
                data: 'dynamicData',
                columnDefs: 'dynamicColumns',
                enablePinning: true,
                filterOptions: { filterText: '', useExternalFilter: false },
                showFilter: true,
                showColumnMenu: true,
                rowHeight: 21,
                selectWithCheckboxOnly: true,
                pinSelectionCheckbox: false,
                enableHighlighting: true,
                selectedItems: $scope.selectedRows
            };
            $scope.startQuiz = function() { $log.warn('$scope.startQuiz() not valid to be called'); };
            var nextInputOuch = function() { $log.warn('$scope.nextInput() not valid to be called'); };
            $scope.nextInput = nextInputOuch;

            data.schemaMapPromise.then(
                function (schemaMap) {
                    var schema = schemaMap[$routeParams.schemaId];
                    $scope.schema = schema;

                    var columnKeys = schema.columns.map(
                        function(col) { return col.id; }
                    );

                    $scope.dynamicData =
                        schema.data.map(function(row) {
                            return _.object(columnKeys, row);
                        });

                    var idCol = _.find(
                        schema.columns, 
                        function(col) {
                            return col.columnType == 'id';
                        }
                    );

                    $scope.dynamicColumns = schema.columns.map(
                        function(col) { 
                            var cellClasses = col.cellClassShort.split(/ +/);
                            cellClasses.push(col.columnType);
                            var colDef = { 
                                field: col.id, 
                                displayName: col.name,
                                resizable: true,
                                sortable: true,
                                pinned: col.columnType == 'id',
                                groupable: col.columnType == 'category',
                                cellClass: cellClasses.map(function(cc) { return 'schemaTable_' + cc;}).join(' ')
                            };
                            if (col.width.indexOf("+") >= 0) {
                                colDef.minWidth = parseInt(col.width);
                                colDef.width = parseInt(col.width);
                            } else {
                                colDef.width = parseInt(col.width);
                                colDef.maxWidth = parseInt(col.width);
                            }
                            return colDef; 
                        }
                    );
                    
                    $scope.startQuiz = function() {
                        var selectedData = 
                            _.flatten(
                                _.map(
                                    $scope.selectedRows,
                                    function(row) {
                                        return _.map(
                                            _.filter(
                                                schema.columns,
                                                function (col) {
                                                    return col.columnType == 'input';
                                                }
                                            )
                                            ,
                                            function (col) {
                                                return { 
                                                    id: row[idCol.id], 
                                                    inputCol: col, 
                                                    expected: row[col.id],
                                                    actual: row[col.id],
                                                    editDistCss: function() {
                                                        var editDistVal = utils.editDist(this.actual, this.expected);
                                                        var res = 
                                                            _.reduce(
                                                                [1, 2, 3, 5, 8, 13],
                                                                function(memo, elem) {
                                                                    return editDistVal >= elem ? memo + 1 : memo;
                                                                },
                                                                0
                                                            );
                                                        console.log('editDist', res);
                                                        return res;
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
                            $scope.quizLog.push($scope.quizCurrent());
                            $scope.quizQueue.splice($scope.quizIdx, 1);
                            $scope.quizIdx = _.random(0, $scope.quizQueue.length - 1);
                        }
                    };
                }
            );
            
        }]
    );
