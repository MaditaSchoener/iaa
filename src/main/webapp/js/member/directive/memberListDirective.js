/*
* @author Madita Schöner
* Referenz-Datei für memberList.html. Verweist auf die zugehörige view und auf den Controller
*/
'use strict';

application.directive('memberList', () => ({
  restrict: 'E',
  transclude: false,
  templateUrl: 'js/member/directive/view/memberList.html',
  controller: 'memberListController'
}));
