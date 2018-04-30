$(document).ready(function() {
	 changePageAndSize();
    // $('#pageSizeSelect').change(function(evt) {
     //    window.location.replace("/userList?pageSize=" + this.value + "&page=1");
    // });
});

function changePageAndSize() {

	$('#pageSizeSelect').change(function(evt) {
		window.location.replace("/userList?pageSize=" + this.value + "&page=1");
      //  alert("hi")
	});
}
