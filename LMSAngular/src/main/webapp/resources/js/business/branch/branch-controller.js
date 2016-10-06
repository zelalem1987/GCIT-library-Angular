lmsModule.controller("branchController", function($scope, $http, $window, $route, lmsRestService, lmsConstants) {
	lmsRestService.get(lmsConstants.BRANCH_VIEW, 
			function(data) {
				$scope.branches = data;
			})
			
	$scope.newBranch = function() {
		var branch = {
			branchName : $scope.branchName,
			branchAddress : $scope.branchAddress
		};		
		lmsRestService.post(lmsConstants.BRANCH_ADD, branch,
				function(data) {
					$scope.message = "Sucess";
					$scope.message = true;
					$window.location.href = lmsConstants.BRANCH_VIEW_HTML_PATH;
				})		
	}
	
	
	$scope.showEdit = function(branchId){
		$http.get(lmsConstants.BRANCH_VIEW_BY_ID + branchId).success(function(data){
			$scope.branch = data;
			$scope.editBranchModal = true;
		})
	}
	
	$scope.updateBranch = function(){
		$http.post(lmsConstants.BRANCH_UPDATE, $scope.branch).success(function(data){
			lmsRestService.get(lmsConstants.BRANCH_VIEW, 
					function(data) {
						$scope.branches = data;
					})
			$scope.editBranchModal = false;
		})
	}
	
	$scope.closeEdit = function(branchId){
		$scope.editBranchModal = false;
	}
	
	
	$scope.deleteBranch = function(index, branchName){
		var deleteBranch = $window.confirm('Are you sure you want to delete Brnach: ' + branchName + ' ?');
		if(deleteBranch){
			$http.post(lmsConstants.BRANCH_DELETE, {branchId: $scope.branches[index].branchId}).success(function(data) {
				lmsRestService.get(lmsConstants.BRANCH_VIEW, 
						function(data) {
							$scope.branches = data;
						})
			})
			
		}
	}
	

})