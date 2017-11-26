/*
// @author Madita Schöner
*/
'use strict';

application.directive('memberList', () => ({
  restrict: 'E',
  transclude: false,
  templateUrl: 'js/member/directive/view/memberList.html',
  controller: 'memberListController'
}));
