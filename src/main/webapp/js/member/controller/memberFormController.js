'use strict';

application.controller('memberFormController', [
  '$scope',
  'memberService',
  'addressService',
  'bankingDetailsService',
  ($scope, memberService, addressService, bankingDetailsService) => {
	 
	  memberService.readMemberships().then(response => $scope.memberships = response.data);
	  
	  var _prevSelectFunction;
	  var _acceptResponseData = (response) => {
		  var member = response.data;
		  $scope.replaceDateArraysWithDates(member);
		  $scope.selectMember(member);
	  };
	  
	  $scope.selectFamilyMember = () => {
		  var checkboxTicked = $scope.hasFamilyMember;
		  if (checkboxTicked) {
			  _prevSelectFunction = $scope.selectMember;
			  $scope.selectMember = (selMember) => {
				  $scope.member.familyMember = selMember;
				  $scope.selectMember = _prevSelectFunction;
				  $scope.getContribution($scope.member);
			  }
		  } else {
			  $scope.member.familyMember = undefined;
			  $scope.selectMember = _prevSelectFunction;
			  $scope.getContribution($scope.member);
		  }  
	  }
	  
	  $scope.editMember = (member) => {
		  $scope.memberEditForm.$setPristine();
		  if (member.number) {
			  memberService.findMember(member.number).then(response => {
				  var member = response.data;
				  $scope.replaceDateArraysWithDates(member);
				  $scope.member = member;
				  $scope.hasFamilyMember = member.familyMember != undefined;
			  });
		  } else {
			  $scope.member = member;
			  $scope.hasFamilyMember = false;
		  }
	  }
	  
	  $scope.saveMember = (member) => {
		  if (!member.number) {
			  memberService.createMember(member).then(response => {
				  $scope.members.push(response.data);
				  _acceptResponseData(response);
			  }, $scope.displayError);
		  } else {
			  memberService.updateMember(member).then(_acceptResponseData, $scope.displayError);
		  }
	  }
	  $scope.cancelMember = (member) => {
		  memberService.cancelMember(member).then(_acceptResponseData);
	  }
	  $scope.deleteMember = (member) => {
		  memberService.deleteMember(member).then(response => {
			  for (var indx in $scope.members) {
				  if ($scope.members[indx].number == response.data.number) {
					  $scope.members.splice(indx);
					  break;
				  }
			  }
			  $scope.selectMember({});
		  }, $scope.displayError);
	  }
	  $scope.getContribution = (member) => {
		  memberService.getContribution(member).then(response => {
			  member.futureContribution = response.data.amount;
		  });
	  }
	  
	  $scope.updateStreets = () => {
		  var start = $scope.member.address.street;
		  addressService.availableStreets(start).then(response => $scope.streets = response.data);
		  $scope.updateZips(start);
	  }
	  $scope.updateZips = (street) => {
		  addressService.availableZips(street).then(response => {
			  $scope.zips = response.data;
		  });
	  }
	  $scope.updateCity = () => {
		  var street = $scope.member.address.street;
		  var zip = $scope.member.address.zip;
		  addressService.findAddress(street, zip).then(response => $scope.member.address.city = response.data.city);
	  }
	  
	  $scope.updateIbans = () => {
		  var iban = $scope.member.bankingDetails.iban;
		  bankingDetailsService.availableIbans(iban).then(response => $scope.availableIbans = response.data);
		  bankingDetailsService.findBankingDetails(iban).then(response => {
			  if (response.data && response.data.iban) {
				  $scope.member.bankingDetails.bic = response.data.bic;
			  }
		  })
	  }
  }
]);
