'use strict';

application.service('memberService', [
  '$http',
  function ($http) {
    this.findAll = () => $http.get('/iaa/api/member');
    this.findMember = (id) => $http.get('/iaa/api/member/find?id=' + id);
    this.searchMember = (member) => $http.post('/iaa/api/member/search', member);
    
    this.createMember = (member) => $http.post('/iaa/api/member/persist', member);
    this.updateMember = (member) => $http.post('/iaa/api/member/update', member);
    this.cancelMember = (member) => $http.post('/iaa/api/member/cancel', member);
    this.deleteMember = (member) => $http.post('/iaa/api/member/delete', member);
    this.getContribution = (member) => $http.post('/iaa/api/member/contribution', member);
    this.readMemberships = () => $http.get('/iaa/api/member/types');
  }
]);

application.service('addressService', [
	'$http',
	function ($http) {
		this.availableStreets = (start) => $http.get('/iaa/api/address/streets?start=' + start);
		this.availableZips = (street) => $http.get('/iaa/api/address/zips?street=' + street);
		this.findAddress = (street, zip) => $http.get('/iaa/api/address/find?street=' + street + '&zip=' + zip);
	}
]);
