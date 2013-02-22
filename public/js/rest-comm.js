
/*
 * Sends a GET request to the server for a list of trends which are added to the sidebar.
 */
function getTrends(){
	var url = "http://localhost:3000/trends";
	$.getJSON(url, function(data){
		trends = data;
		loadSideBar(trends);
		console.log(trends);
	});	
}

