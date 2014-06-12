'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', [
  'ngRoute',
  'ngGrid',
  'myApp.filters',
  'myApp.services',
  'myApp.directives',
  'myApp.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/schema/:schemaId', {templateUrl: 'partials/schema.html', controller: 'SchemaTableController'});
  $routeProvider.otherwise({redirectTo: '/view1'});
}]);
