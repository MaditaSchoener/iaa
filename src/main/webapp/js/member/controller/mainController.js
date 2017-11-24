'use strict';

application.controller('mainController', [
  '$scope',
  ($scope) => {
		  
	  $scope.print = (elementId) => {
		  var element = document.getElementById(elementId);
		  
//		  write html string that contains the same styling as the application
		  var printContent = '<html><head>'
	  		+'<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css"' 
	  		+'integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">'
	  		+ '<link rel="stylesheet" href="styles.css"></head><body onload="window.print()">'
	  		+ element.outerHTML + '</body></html>';
		  var popupWin = window.open('Drucken', '_blank', 'width=960,height=600');
		  popupWin.document.open();
		  popupWin.document.write(printContent);
		  popupWin.document.close();
	  }
	  
	  $scope.replaceDateArraysWithDates = (member) => {
		  member.birth = new Date(member.birth[0], member.birth[1] - 1, member.birth[2]);
	  }
	  
	  $scope.displayError = (response) => {
		  $scope.messages = response.data.messages;
	  }
  }
]);
