lmsModule.controller("genreController", function($scope, $http, $window, $route, lmsRestService, lmsConstants) {
	lmsRestService.get(lmsConstants.GENRE_VIEW, 
			function(data) {
				$scope.genres = data;
			})
			
	$scope.newGenre = function() {
		var genre = {
				genreName : $scope.genreName
		};		
		lmsRestService.post(lmsConstants.GENRE_ADD, genre,
				function(data) {
					$scope.message = "Sucess";
					$scope.message = true;
					$window.location.href = lmsConstants.GENRE_VIEW_HTML_PATH;
				})		
	}
	
	
	$scope.showEdit = function(genre_id){
		$http.get(lmsConstants.GENRE_VIEW_BY_ID + genre_id).success(function(data){
			$scope.genre = data;
			$scope.editGenreModal = true;
		})
//		$window.confirm(authorID);
	}
	
	$scope.updateGenre = function(){
		$http.post(lmsConstants.GENRE_UPDATE, $scope.genre).success(function(data){
			lmsRestService.get(lmsConstants.GENRE_VIEW, 
					function(data) {
						$scope.genres = data;
					})
			$scope.editGenreModal = false;
		})
	}
	
	$scope.closeEdit = function(genre_id){
		$scope.editGenreModal = false;
	}
	
	
	$scope.deleteGenre = function(index, genre_name){
		var deleteGenre = $window.confirm('Are you sure you want to delete Author: ' + genre_name + ' ?');
		if(deleteGenre){
			$http.post(lmsConstants.GENRE_DELETE, {genre_id: $scope.genres[index].genre_id}).success(function(data) {
				lmsRestService.get(lmsConstants.GENRE_VIEW, 
						function(data) {
							$scope.genres = data;
						})
			})
			
		}
	}
	

})