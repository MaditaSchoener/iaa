
/*
* @author Madita Schöner
* Referenz-Datei für contributionList.html. Verwist auf die zugehörige view und auf den Controller
*/
'use strict';

application.directive('contributionList', () => ({
  restrict: 'E',
  transclude: false,
  templateUrl: 'js/member/directive/view/contributionList.html',
  controller: 'contributionListController'
}));
