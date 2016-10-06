lmsModule.controller("authorController", function($scope, $http, $window, $route, lmsRestService, lmsConstants) {
	lmsRestService.get(lmsConstants.AUTHOR_VIEW, 
			function(data) {
				$scope.authors = data;
			})
			
	$scope.newAuthor = function() {
		var author = {
			authorName : $scope.authorName			
		};		
		lmsRestService.post(lmsConstants.AUTHOR_ADD, author,
				function(data) {
//	Add Success message here
					$scope.message = "Sucess";
					$scope.message = true;
					$window.location.href = lmsConstants.AUTHOR_VIEW_HTML_PATH;
				})		
	}
	
	
	$scope.showEdit = function(authorID){
		$http.get(lmsConstants.AUTHOR_VIEW_BY_ID + authorID).success(function(data){
			$scope.author = data;
			$scope.editAuthorModal = true;
		})
//		$window.confirm(authorID);
	}
	
	$scope.updateAuthor = function(){
		$http.post(lmsConstants.AUTHOR_UPDATE, $scope.author).success(function(data){
			lmsRestService.get(lmsConstants.AUTHOR_VIEW, 
					function(data) {
						$scope.authors = data;
					})
			$scope.editAuthorModal = false;
		})
	}
	
	$scope.closeEdit = function(authorID){
		$scope.editAuthorModal = false;
	}
	
	//deleteAuthor
	$scope.deleteAuthor = function(authorId, authorName){
		var deleteAuthor = $window.confirm('Are you sure you want to delete Author: ' + authorName + ' ?');
		if(deleteAuthor){
			$http.post(lmsConstants.AUTHOR_DELETE, {authorId}).success(function(data) {
				lmsRestService.get(lmsConstants.AUTHOR_VIEW, 
						function(data) {
							$scope.authors = data;
						})
			})
			
		}
	}
	

})