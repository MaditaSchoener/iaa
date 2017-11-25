'use strict';

application.directive('contributionList', () => ({
  restrict: 'E',
  transclude: false,
  templateUrl: 'js/member/directive/view/contributionList.html',
  controller: 'contributionListController'
}));
