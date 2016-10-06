lmsModule.controller("bookController", function($scope, $http, $window, $route, lmsRestService, lmsConstants) {
	lmsRestService.get(lmsConstants.BOOK_VIEW, 
			function(data) {
				$scope.books = data;
				$scope.publishers = data;
				$scope.authors = data;
				$scope.genres = data;
			})
			
	lmsRestService.get(lmsConstants.AUTHOR_VIEW, 
			function(data) {				
				$scope.authorBook = data;				
			})
			
	lmsRestService.get(lmsConstants.PUBLISHER_VIEW, 
			function(data) {				
				$scope.publisherBook = data;				
			})
			
	lmsRestService.get(lmsConstants.GENRE_VIEW, 
			function(data) {				
				$scope.genresBook = data;				
			})
			
	$scope.newBook = function() {
		var book = {
			title : $scope.addTitle,
			publisher : $scope.addPublisher,
			authors : $scope.addAuthor,
			genres : $scope.addGenre
		};		
		lmsRestService.post(lmsConstants.BOOK_ADD, book,
				function(data) {
//	Add Success message here
					$scope.message = "Sucess";
					$scope.message = true;
					$window.location.href = lmsConstants.BOOK_VIEW_HTML_PATH;
				})		
	}
	
	
	$scope.showEdit = function(bookId){
	
		$http.get(lmsConstants.BOOK_VIEW_BY_ID + bookId).success(function(data){
			$scope.book = data;
			console.log($scope.book);
			$scope.editBookModal = true;
		
//			var selectedOptions = [];
//
//		    $scope.book.authors.forEach(function (obj, idx) {
//		    $scope.authorBook.forEach(function (author, idj) {
//		        if (obj.id === author.id) {
//		            selectedOptions.push($scope.authors[idj]);
//		            $scope.book.authors = selectedOptions;
//		        }
//		    })});
		});
		
		
//		$window.confirm(bookId);
	}
	
//	$scope.updateBook = function(){
//		$http.post(lmsConstants.BOOK_UPDATE, $scope.book).success(function(data){
//			lmsRestService.get(lmsConstants.BOOK_VIEW, 
//					function(data) {
//				title : $scope.book.title,
//				bookId :$scope.book.bookId,
//				publisher : $scope.book.publisher,
//				authors : $scope.book.authors,
//				genres : $scope.book.genres
//					})
//			$scope.editBookModal = false;
//		})
//	}
	
//	 {
//			'bookId' : $scope.book.bookId,
//			'title' : $scope.book.title,
//			'publisher' : $scope.book.publisher,
//			'authors' : $scope.book.authors,
//			'genres' : $scope.book.genres
//		}
//	
	$scope.updateBook = function updateBook() {
		console.log($scope.book);
		$http.post(lmsConstants.BOOK_UPDATE,$scope.book).success(function(data) {
			lmsRestService.get(lmsConstants.BOOK_VIEW, 
					function(data) {
						$scope.books = data;
						$scope.publishers = data;
						$scope.authors = data;
						$scope.genres = data;
						alert('Book Edited Successfully');
					})
		});

		lmsRestService.get(lmsConstants.BOOK_VIEW, 
				function(data) {
		$scope.editBookModal = false;
	})

	};
	
	$scope.closeEdit = function(bookId){
		$scope.editBookModal = false;
	}
	
	//deleteAuthor
	$scope.deleteBook = function(index, title){
		var deleteBook = $window.confirm('Are you sure you want to delete Book: ' + title + ' ?');
		if(deleteBook){
			$http.post(lmsConstants.BOOK_DELETE, {bookId: $scope.books[index].bookId}).success(function(data) {
//				$window.confirm(title + ' has been deleted!');	
				lmsRestService.get(lmsConstants.BOOK_VIEW, 
						function(data) {
							$scope.books = data;
						})
			})
			
		}
	}
	

})