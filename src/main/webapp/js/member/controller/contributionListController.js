'use strict';

application.controller('contributionListController', [
  '$scope',
  ($scope) => {
	  
	  $scope.initContributionList = () => {
//		  Javascript offsets dates by 1900
		  $scope.readContributionsForYear (new Date().getYear() + 1900);
		  $scope.contributionListVisible = true;
	  }
	  
	  $scope.readContributionsForYear = (year) => {
		  $scope.displayedYear = year;
		  $scope.membersWithContributioInYear = $scope.members.filter(member => {
			  var contribution = member.contributions[year];
			  return contribution!= undefined && contribution.amount != undefined;
		  });
	  }
  }
]);
