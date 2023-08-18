function deleteingredient(id) {
	console.log("deleting"+id);
	$.ajax({
            url: '/api/v1/ingredient/ingredient/'+id,
            type: 'DELETE',
            success: function (result) {
                // Do something with the result
                location.reload();
            }
        });
}