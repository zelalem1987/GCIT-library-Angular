lmsModule.factory("BookFactory", function($http){
	var factory = {};
	
	factory.get = function(path, callback){
		return $http.get("http://localhost:8080/lms/"+path).success(callback);
	}
	
	return factory;
})

lmsModule.service("BookService", function(BookFactory){
	this.get = function(path, callback){
		return BookFactory.get(path, callback);
	}
})