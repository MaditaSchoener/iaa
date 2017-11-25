'use strict';

application.controller('memberListController', [
  '$scope',
  'memberService',
  ($scope, memberService) => {
	  
	  var _acceptMemberList = (list) => {
		  for (var member of list) {
			  $scope.replaceDateArraysWithDates(member);
		  }
		  $scope.members = list;
	  };
	  
	  memberService.findAll().then(response => _acceptMemberList(response.data));
	  
	  $scope.searchMember = (member) => {
		  if (member == undefined) {
			  member = {};
		  }
		  memberService.searchMember(member).then(response => {
			  	_acceptMemberList(response.data);
		  		$scope.searchOpened = false;
		  });
	  }
	  
	  $scope.resetSearch = () => {
		  $scope.memberToSearch = {};
		  $scope.searchMember ($scope.memberToSearch);
	  }
	  
	  $scope.selectMember = (member) => {
		  if ($scope.selectedMember == member) {
			  $scope.selectedMember = {};
		  } else {
//			  if the memberToSelect is not in the list replace the deprecated member with the new one
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
