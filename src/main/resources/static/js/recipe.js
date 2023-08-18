

/*let modalAccept = document.getElementById('modalAccept')

modalAccept.addEventListener('click', function() {
	const buttonId = this.id
	document.getElementById('modalform').submit()
})
*/

function labelClicked(id) {
	console.log(id);
}


$('#modal3').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget) // Button that triggered the modal
	var recipe = button.data('whatever') // Extract info from data-* attributes
	// If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
	// Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
	var modal = $(this)
	//modal.find('.modal-title').text('New ingredient for:  ' + recipe)
	//modal.find('.mydate input').val(date)
})

function filldropdown() {
let dropdown = $('#ingredient');

dropdown.empty();

dropdown.append('<option selected="true" disabled>Choose Ingredient</option>');
dropdown.prop('selectedIndex', 0);

const url = '/api/v1/ingredient/ingredient';

// Populate dropdown with list of ingredientsd
$.getJSON(url, function (data) {
  $.each(data, function (key, entry) {
    dropdown.append($('<option></option>').attr('value', entry.id).text(entry.name + ' (' + entry.uom +')'));
  })
});
}

function addIngredient() {
	//var ingredient = document.getElementById('ingredient').value()
//	var quantity = document.getElementById('quantity').value()
	
	const url = 'api/v1/ingredientquantity/';
	var ingredientId = $("#ingredient").val();
    var quantity = $("#quantity").val();
    var recipeId = $("#recipeId").val();
    var ingredientSelect = document.getElementById("ingredient");
    var ingredientName = ingredientSelect.options[ingredientSelect.selectedIndex].text;
   // var ingredientName = $("#ingredient").text();
    
    var sendInfo = {
    	"ingredientId": ingredientId,
    	"quantity": quantity,
    	"recipeId": recipeId
    };
    
    var data = new Object();
    data.ingredientId = ingredientId;
    data.quantity = quantity;
    data.recipeId = recipeId;
    
    var myString = JSON.stringify(data);
    
    $.ajax({
    	  url:url,
    	  type:"POST",
    	  data:myString,
    	  success: $('#alreadyadd').append('<li>'+ingredientName+'</li>'),
    	  error: function (jqXHR, textStatus, errorThrown) {
              alert('Erreur: '+jqXHR.responseText);
          },
    	  contentType:"application/json; charset=utf-8",
    	  dataType:"json"
    	})
	
}