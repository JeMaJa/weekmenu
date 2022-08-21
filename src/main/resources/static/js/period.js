let modalAccept = document.getElementById('modalAccept')

modalAccept.addEventListener('click', function () {
							const buttonId = this.id
							document.getElementById('modalform').submit()
							})
							
							
$('#modal1').on('show.bs.modal', function (event) {
	  var button = $(event.relatedTarget) // Button that triggered the modal
	  var date = button.data('whatever') // Extract info from data-* attributes
	  // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
	  // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
	  var modal = $(this)
	  modal.find('.modal-title').text('New recipe for:  ' + date)
	  modal.find('.mydate input').val(date)
	})