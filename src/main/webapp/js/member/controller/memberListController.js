/*
* @author Madita Schöner
*
* Controller für memberList.html
*/
'use strict';

application.controller('memberListController', [
  '$scope',
  'memberService',
  ($scope, memberService) => {

	/*
	* Normalisiert alle vom Server gelieferten Dates aus der Mitgliederliste
	* @private
	* @param list {array} Mitgliederliste
	* */
	  var _acceptMemberList = (list) => {
		  for (var member of list) {
			  $scope.replaceDateArraysWithDates(member);
		  }
		  $scope.members = list;
	  };

	/*
	* Abrufen der Mitglieder und Normalisierung der Dates
	* @public
	* @param list {array} Mitgliederliste
	* */
	  memberService.findAll().then(response => _acceptMemberList(response.data));

	/*
	* Übergibt die Filterkriterien an den memberService
	* @public
	* @param member {Object} Filterkriterium
	* */
	  $scope.searchMember = (member) => {
		  if (member == undefined) {
			  member = {};
		  }
		  memberService.searchMember(member).then(response => {
			  	_acceptMemberList(response.data);
		  		$scope.searchOpened = false;
		  });
	  }

	/*
	* Leeren der Filterkriterien
	* @public
	* */
	  $scope.resetSearch = () => {
		  $scope.memberToSearch = {};
		  $scope.searchMember ($scope.memberToSearch);
	  }

	/*
	* Setzen des ausgewählten Mitglieds und Setzen des zu bearbeitenden Mitglieds
	*
	* @public
	* @param member {Object} Mitglied
	* */
	  $scope.selectMember = (member) => {
		  if ($scope.selectedMember == member) {
			  $scope.selectedMember = {};
		  } else {
//			  Wenn das ausgewählte Mitglied nicht in der Liste enthalten ist, wird das vorherige Mitglied durch das neue ersetzt
			  for (var indx in $scope.members) {
				  if (member.number == $scope.members[indx].number) {
					  $scope.members[indx] = member;
					  break;
				  }
			  }
			  $scope.selectedMember = member;
		  }	
		  $scope.editMember($scope.selectedMember);
	  }
  }
]);
