/*global require, requirejs */

var akkaJS = angular.module('akkaJS', []);

akkaJS.controller('ScriptListCtrl', function ($scope) {
  $scope.scripts = [
    {
     'typeName': 'echo',
     'code': 'print("babam");'
    }
  ];
});