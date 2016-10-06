lmsModule.controller("borrowerController", function($scope, $http, $window, $route,$rootScope,
		lmsRestService, lmsConstants, shared, Books, AuthorService, BookService) {
	
	
	lmsRestService.get(lmsConstants.BORROWER_VIEW, 
			function(data) {
				$scope.borrowers = data;
			})			
	
		
	lmsRestService.get(lmsConstants.OVERWRITE_LOAN, 
				function(data) {
					$scope.bookLoanList = data;
				})
			
	$scope.newBorrower = function() {
		var borrower = {
			borrowerName : $scope.borrowerName,
			borrowerAddress : $scope.borrowerAddress,
			borrowerPhone : $scope.borrowerPhone
		};		
		lmsRestService.post(lmsConstants.BORROWER_ADD, borrower,
				function(data) {
//	Add Success message here
					$scope.message = "Sucess";
					$scope.message = true;
					$window.location.href = lmsConstants.BORROWER_VIEW_HTML_PATH;
				})		
	}
	
	$scope.showBorrowerEdit = function(borrowerCardNo){
		$http.get(lmsConstants.BORROWER_VIEW_BY_ID + borrowerCardNo).success(function(data){
			$scope.borrower = data;
			$scope.editPublisherModal = true;
		})
//		$window.confirm(borrowerCardNo);
	}
	
	$scope.updateAuthor = function(){
		$http.post(lmsConstants.BORROWER_UPDATE, $scope.borrower).success(function(data){
			lmsRestService.get(lmsConstants.BORROWER_VIEW, 
					function(data) {
						$scope.borrower = data;
					})
			$scope.editPublisherModal = false;
		})
	}
	
	$scope.closeEdit = function(borrowerCardNo){
		$scope.editPublisherModal = false;
	}
	
	//deleteAuthor
	$scope.deleteBorrower = function(index, borrowerName){
		var deleteBorrower = $window.confirm('Are you sure you want to delete Borrower: ' + borrowerName + ' ?');
		if(deleteBorrower){
			$http.post(lmsConstants.BORROWER_DELETE, {borrowerCardNo: $scope.borrowers[index].borrowerCardNo}).success(function(data) {
				lmsRestService.get(lmsConstants.BORROWER_VIEW, 
						function(data) {
							$scope.borrowers = data;
						})
			})
			
		}
	}
	
	$scope.logIn = function() {
		var borrower = {
				borrowerCardNo : $scope.cardNo
		};
		
		$scope.card = $scope.cardNo;

		$http.post("http://localhost:8080/lms/checkId", borrower).success(
				function(data) {

					log = data;
//					alert(data + " "+borrower.borrowerCardNo)
					if (data == "true") {
						
						shared.setCardNo(borrower.borrowerCardNo);
						
						$window.location.href = "#/borrowerMenu";
					} else {
						alert("Card Number not register!  please try again");
						//location.reload();
						$window.location.href = "#/borrower";

					}
				})

	}
	
	$scope.backToBorrowers = function() {
						
		$window.location.href = "#/borrowerMenu";

	}
	
//	AuthorService.get("/viewbranch", function(data) {
//
//		$scope.branchs = data;
//
//	});
	
	lmsRestService.get("http://localhost:8080/lms/viewbranch", 
			function(data) {
				$scope.branchs = data;
			})
	
	var loan = {
			cardNo : shared.getCardNo(),
			branchId : shared.getBranchId()
		}

		
		$scope.brSelection = function() {
		
//		console.log("loan branchId "+ loan.branchId);
			$window.location.href = "#/branchselection";
		}
	
		$http.get("http://localhost:8080/lms/viewBranchBook/" + loan.branchId).success(
			function(data) {
				
				$scope.branchBooks = data;
				

			})

//		 $scope.branchB = Books.getBook;
		$scope.boSelection = function(branchId) {

			$scope.branchId = branchId;
			shared.setBranchId(branchId);
			$window.location.href = "#/bookselection";
		}
		console.log("branchId before the checkout function: " + shared.getBranchId())

		$scope.checkOut = function(bookId) {
			$scope.bookId = bookId;
			var l = {
				branchId : shared.getBranchId(),
				bookId : bookId,
				cardNo : shared.getCardNo()
			}

			console.log("branchId " + l.branchId)
//			console.log("bookId " + l.bookId)
//			console.log("cardNo " + l.cardNo)
			$http.post("http://localhost:8080/lms/checkeOut", l).success(
					function(data) {
							alert(data.message);
							$http.get("http://localhost:8080/lms/viewBranchBook/" + shared.getBranchId()).success(
									function(data) {
										$scope.branchBooks = data;	

										console.log("loan branchId "+ shared.getBranchId());
										$window.location.href = "#/bookselection"
									})		
					})

		}
		$http.get("http://localhost:8080/lms/checkInBranch/" + shared.getCardNo())
				.success(function(data) {
					$scope.bookLoanInfo = data;

				})
				
//				var l = {
//			branchId : shared.getBranchId(),
//			bookId : Books.getBookId(),
//			cardNo : shared.getCardNo()
//		}
		
		$scope.checkInBranchs = function() {
			$window.location.href = "#/checkInBranch"

		}	

		
		$scope.returnBook = function(bookId) {
			var loan2 = {
				branchId : shared.getBranchId(),
				bookId : bookId,
				cardNo : shared.getCardNo()
			}

			$http.post("http://localhost:8080/lms/return", loan2).success(
					function(data) {
						alert("Book returned successfully");

//						console.log("branchId " +shared.getBranchId())
//						console.log("bookId " + loan2.bookId)
//						console.log("cardNo " + loan2.cardNo)
						$window.location.href = "#/borrowerMenu"

					})
		}
		
		
//		var lo = {
//			branchId : shared.getBranchId(),
//			cardNo : shared.getCardNo()
//		};
//
//		$http.post("http://localhost:8080/lms/checkInBook", lo).success(
//				function(data) {
//
//					$scope.cBook = data;
//
//				})
//		$scope.checkInBook = function(branchId) {
//
//			shared.setBranchId(branchId)
//
//			$window.location.href = "#/checkBook"
//		}
	

})