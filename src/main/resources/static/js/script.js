/*$(document).ready(function() {
	$('#verifyOTP').on('submit', function(event) {
		event.preventDefault();
		let id = $('.verifybtn').attr('id');
		let form = new FormData(this);
		$.ajax({
			url: "/verifier/verifyOTP/" + id,
			type: 'post',
			data: form,
			success: function(data, textStatus, jqXHR) {
				console.log(data);
				console.log("done");
			},
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR);
				console.log("error");
			},
			processData: false,
			contentType: false
		})

	})
});*/
