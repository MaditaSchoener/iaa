/**
* @author Madita Schöner
*
* Controller für memberForm.html
*/

'use strict';

application.controller('memberFormController', [
  '$scope',
  'memberService',
  'addressService',
  'bankingDetailsService',
  ($scope, memberService, addressService, bankingDetailsService) => {
	 
	  memberService.readMemberships().then(response => $scope.memberships = response.data);
	/**
	* Setzt das neu erstellte Mitglied als ausgewähltes Objekt der Liste. Und ersetzt
	* die vom Server gelieferten Dates mit Java Script Dates des neuen Mitglieds
	* @private
	* @param response {Object}  Mitglied
	* */
	  var _prevSelectFunction;
	  var _acceptResponseData = (response) => {
		  var member = response.data;
		  $scope.replaceDateArraysWithDates(member);
		  $scope.selectMember(member);
	  };

	/**
	* Aktion beim Auswählen eines Familienmitglieds. Übergabe der Familienmitgliedparameter
	* @public
	* */
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

	/**
	* Zurücksetzen des Mitgliedformulars
	* Setzen des zu bearbeitenden Mitglieds
	* @public
	* @param member {Object} Mitglied
	* */
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

	/**
	* Speichern des Mitglieds. Falls keine Mitgliedsnummer übergeben wurde, erstellllt der
	* memberService ein neues Mitglied. Andernfalls werden die Daten aktualisiert
	* @public
	* @param member {Object} Mitglied
	* */
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

	/**
	* Übergabe des Mitglieds an den memberService zur Kündigung der Mitgliedschaft
	* @public
	* @param member {Object}  Mitglied
	* */
	  $scope.cancelMember = (member) => {
		  memberService.cancelMember(member).then(_acceptResponseData);
	  }
	/**
	* Übergabe des Objekts an den memberService zum Löschen des Objektes und
	* Anpassung des Indexes der Mitgliederliste
	* @public
	* @param member {Object} Mitglied
	* */
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
	/**
	* Ermittlung des zukünftigen Beitrages
	* @public
	* @param member {Object} Mitglied
	* */
	  $scope.getContribution = (member) => {
		  memberService.getContribution(member).then(response => {
			  member.futureContribution = response.data.amount;
		  });
	  }

	/**
	* Anzeigen der zur verfügung stehenden Straßen als Vorschlag
	* @public
	* */
	  $scope.updateStreets = () => {
		  var start = $scope.member.address.street;
		  addressService.availableStreets(start).then(response => $scope.streets = response.data);
		  $scope.updateZips(start);
	  }
	/**
	* Anzeigen der zur verfügung stehenden PLZ als Vorschlag
	* @public
	* @param street {String} Straße
	* */
	  $scope.updateZips = (street) => {
		  addressService.availableZips(street).then(response => {
			  $scope.zips = response.data;
		  });
	  }
	/**
	* Anzeigen der zur verfügung stehenden Orte als Vorschlag
	* @public
	* */
	  $scope.updateCity = () => {
		  var street = $scope.member.address.street;
		  var zip = $scope.member.address.zip;
		  addressService.findAddress(street, zip).then(response => $scope.member.address.city = response.data.city);
	  }

	/**
	* Anzeigen der zur verfügung stehenden IBANs als Vorschlag
	* @public
	* */
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
