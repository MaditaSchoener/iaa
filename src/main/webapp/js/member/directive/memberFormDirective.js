/**
* @author Madita Schöner
* Referenz-Datei für memberForm.html. Verweist auf die zugehörige view und auf den Controller
*/
'use strict';

application.directive('memberForm', () => ({
  restrict: 'E',
  transclude: false,
  templateUrl: 'js/member/directive/view/memberForm.html',
  controller: 'memberFormController'
}));
