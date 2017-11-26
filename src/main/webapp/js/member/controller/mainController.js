/*
// @author Madita Schöner
// Controller für die main.html
*/

'use strict';

application.controller('mainController', [
  '$scope',
  ($scope) => {
    /*
	* Öffnet eine Druckseite der Mitglieder, mit dem gleichen Styling der Application
	* @public
	* @param elementId MitgliederListe (memberList) als element
	* */
	  $scope.print = (elementId) => {
		  var element = document.getElementById(elementId);

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

	/*
	* Öffnet eine Druckseite der Mitglieder, mit dem gleichen Styling der Application
	* @public
	* @param elementId MitgliederListe (memberList) als element
	* */
	  $scope.replaceDateArraysWithDates = (member) => {
		  member.birth = new Date(member.birth[0], member.birth[1] - 1, member.birth[2]);
	  }

/*	* Anzeigen der Error-Nachrichten
	* @public
	* @param Objekt der Fehlermeldung
	* */
	  $scope.displayError = (response) => {
		  $scope.messages = response.data.messages;
	  }
  }
]);
