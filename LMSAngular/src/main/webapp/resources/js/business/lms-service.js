lmsModule.service("lmsRestService", function($http){
	this.get = function(url, callBack){
		return $http.get(url).success(callBack);
	}
	
	this.post = function(url, obj, callBack){
		return $http.post(url, obj).success(callBack);
	}
})