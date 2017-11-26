/*
* @author Madita Schöner
*
* Controller für die contributionList.hmtl
*/


'use strict';

application.controller('contributionListController', [
  '$scope',
  ($scope) => {

	/*
	* Öffnet die Beitragsliste für das aktuelle Jahr
	* @public
	* */
	  $scope.initContributionList = () => {
//		  Javascript offsets dates by 1900
		  $scope.readContributionsForYear (new Date().getYear() + 1900);
		  $scope.contributionListVisible = true;
	  }

	/*
	* Ändert das Default Jahr (aktuelles Jahr) auf ein gesetztes und filtert die Mitglieder die
	* in dem gesetzten Jahr einen Betrag zahlen muss
	* @public
	* @param year {Integer} das Jahr, welches als Filterkriterium gesetzt wird
	* @param Contribution {Object} das Jahr, welches als Filterkriterium gesetzt wird
	* @returns {Array} gefilterte Mitglieder und deren zu entrichtende Beiträge für das gesetzte Jahr
	* */
$scope.readContributionsForYear = (year) => {
		  $scope.displayedYear = year;
		  $scope.membersWithContributioInYear = $scope.members.filter(member => {
			  var contribution = member.contributions[year];
			  return contribution!= undefined && contribution.amount != undefined;
		  });
	  }
  }
]);
