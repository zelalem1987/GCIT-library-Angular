lmsModule.factory("AuthorFactory", function($http){
	var factory = {};

	factory.get = function(path, callback,data){
		return $http.get("http://localhost:8080/lms/"+path).success(callback);
	}
	factory.post=function(path,br , callback){
		
		return $http.post("http://localhost:8080/lms/"+path,br ).success();
	}
	return factory;
})


lmsModule.service("AuthorService", function(AuthorFactory){
	this.get = function(path, callback,data){
		return AuthorFactory.get(path, callback);
	}
	this.post = function(path, br){
		return AuthorFactory.post(path, br);
	}
})


 lmsModule.service('shared', function () {
        var cardNo;
        var brId;

        return {
            getCardNo: function () {
                return cardNo;
            },
            setCardNo: function(value) {
               cardNo  = value;
            },
            getBranchId: function(){
            	return brId;
            },
            setBranchId: function(value){
            	brId = value;
            }
        };
    });
 
lmsModule.service('Books', function () {
    
     var branch;
     var bookId;
     var boId;

     return {
         getBranchId: function () {
             return branch;
         },
         setBranchId: function(value) {
           branch=value;
         },
         getBookId: function () {
             return bookId;
         },
         setBookId: function(value) {
            bookId  = value;
         }
         ,
         getBoId: function () {
             return boId;
         },
         setBoId: function(value) {
            boId  = value;
         }
       
     };
 });