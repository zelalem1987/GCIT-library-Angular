lmsModule.controller("publisherController", function($scope, $http, $window, $route, lmsRestService, lmsConstants) {
	lmsRestService.get(lmsConstants.PUBLISHER_VIEW, 
			function(data) {
				$scope.publishers = data;
			})
			
	$scope.newPublisher = function() {
		var publisher = {
				publisherName : $scope.publisherName,
				publisherAddress : $scope.publisherAddress,
				publisherPhone : $scope.publisherPhone
		};		
		lmsRestService.post(lmsConstants.PUBLISHER_ADD, publisher,
				function(data) {
//	Add Success message here
					$scope.message = "Sucess";
					$scope.message = true;
					$window.location.href = lmsConstants.PUBLISHER_VIEW_HTML_PATH;
				})		
	}
	
	$scope.showPublisherEdit = function(publisherId){
		$http.get(lmsConstants.PUBLISHER_VIEW_BY_ID + publisherId).success(function(data){
			$scope.publisher = data;
			$scope.editPublisherModal = true;
		})
	}
	
	$scope.updatePublisher = function(){
		$http.post(lmsConstants.PUBLISHER_UPDATE, $scope.publisher).success(function(data){
			lmsRestService.get(lmsConstants.PUBLISHER_VIEW, 
					function(data) {
						$scope.publishers = data;
					})
			$scope.editPublisherModal = false;
		})
	}
	
	$scope.closeEdit = function(publisherId){
		$scope.editPublisherModal = false;
	}
	
	//deleteAuthor
	$scope.deletePublisher = function(index, publisherName){
		var deletePublisher = $window.confirm('Are you sure you want to delete Publisher: ' + publisherName + ' ?');
		if(deletePublisher){
			$http.post(lmsConstants.PUBLISHER_DELETE, {publisherId: $scope.publishers[index].publisherId}).success(function(data) {
				lmsRestService.get(lmsConstants.PUBLISHER_VIEW, 
						function(data) {
							$scope.publishers = data;
						})
			})
			
		}
	}
	

})