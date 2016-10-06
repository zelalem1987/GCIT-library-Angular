lmsModule.config([ "$routeProvider", function($routeProvider) {
	return $routeProvider.when("/", {
		redirectTo : "/home"
	}).when("/home", {
		templateUrl : "home.html"
	})
	
	.when("/admin", {
		templateUrl : "admin.html"
	})
	
	.when("/newAuthor", {
		templateUrl : "addauthor.html"
	}).when("/listAuthors", {
		templateUrl : "viewauthors.html"
	})
	
	.when("/newPublisher", {
		templateUrl : "addpublisher.html"
	}).when("/listPublishers", {
		templateUrl : "viewpublishers.html"
	})
	
	.when("/newBorrower", {
		templateUrl : "addborrower.html"
	}).when("/listBorrowers", {
		templateUrl : "viewborrowers.html"
	})
	.when("/overWriteList", {
		templateUrl : "overwriteborrowList.html"
	})
	
	.when("/newBranch", {
		templateUrl : "addbranch.html"
	}).when("/listBranches", {
		templateUrl : "viewbranches.html"
	})
	
	.when("/newGenre", {
		templateUrl : "addgenre.html"
	}).when("/listGenres", {
		templateUrl : "viewgenres.html"
	})
	
	.when("/newBook", {
		templateUrl : "addbook.html"
	}).when("/listBooks", {
		templateUrl : "viewbooks.html"
	})
	
//	.when("/checkOut", {
//		templateUrl : "checkoutbook.html"
//	})
	
	.when("/borrower", {
		templateUrl : "borrowerlogin.html"
	})
	.when("/borrowerMenu", {
		templateUrl : "borrowerspage.html"
	})
	.when("/branchselection", {
		templateUrl : "branchselection.html"
	})
	.when("/bookselection", {
		templateUrl : "bookselection.html"
	})
	.when("/checkInBranch", {
		templateUrl : "checkinbranch.html"
	})
	.when("/checkBook", {
		templateUrl : "checkinbook.html"
	}).when("/LbookSelection", {
		templateUrl : "lbookselection.html"
	}).when("/LBranch", {
		templateUrl : "lbranch.html"
	}).when("/borrowerSelect", {
		templateUrl : "overborrow.html"
	}).when("/bookSelect", {
		templateUrl : "overbook.html"
	}).when("/branchSelect", {
		templateUrl : "overbranch.html"
	})

} ])