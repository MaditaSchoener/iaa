'use strict';

application.directive('memberForm', () => ({
  restrict: 'E',
  transclude: false,
  templateUrl: 'js/member/directive/view/memberForm.html',
  controller: 'memberFormController'
}));
