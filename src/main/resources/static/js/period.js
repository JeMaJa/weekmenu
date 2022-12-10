/*modal stuff */
let modalAccept = document.getElementById('modalAccept')

modalAccept.addEventListener('click', function() {
	const buttonId = this.id
	document.getElementById('modalform').submit()
})


$('#modal1').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget) // Button that triggered the modal
	var date = button.data('whatever') // Extract info from data-* attributes
	// If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
	// Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
	var modal = $(this)
	modal.find('.modal-title').text('New recipe for:  ' + date)
	modal.find('.mydate input').val(date)
})

$('#modal2').on('show.bs.modal', function(event) {
	var button2 = $(event.relatedTarget) // Button that triggered the modal
	var recipeId = button2.data('id') // Extract info from data-* attributes


	// Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
	var modal = $(this)



	var url = 'api/v1/recipe?id=' + recipeId;

	$.getJSON(url, function(result) {
		console.log(result)
		var rname = result.recipeName;
		var descr = result.description;
		var hs = result.healthScore;
		//modal.find('.modal2-title').text(rname)
		modal.find('.modal-title').text(rname)
		modal.find('.modal-body').text(descr)
	});
})

/*
Accept recipe stuff

$('#accept').click(function() {
    var button = $(event.currentTarget) // Button that triggered the modal  
    var id = button.context.dataset.whatever;
	console.log(id);
});
*/
function acceptSuggestion() {
	var button = $(event.currentTarget) // Button that triggered the modal  
    var id = button.context.dataset.whatever;
	console.log(id);
	$.ajax({
            url: '/api/v1/dayrecipe/accept/'+id,
            type: 'PUT',
            success: function (result) {
                // Do something with the result
                location.reload();
            }
        });
}

function newSuggestion() {
	var button = $(event.currentTarget) // Button that triggered the modal  
    var id = button.context.dataset.whatever;
	console.log(id);
	$.ajax({
            url: '/api/v1/dayrecipe/suggest/'+id,
            type: 'PUT',
            success: function (result) {
                // Do something with the result
                location.reload();
            }
        });
}

/*
DATE RANGE LICENSE:
https://www.daterangepicker.com/

The MIT License (MIT)

Copyright (c) 2012-2019 Dan Grossman

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

*/
$(function() {
	$('input[name="daterange"]')
		.daterangepicker({
    "showDropdowns": true,
    "showWeekNumbers": true,
    ranges: {
        'Today': [moment(), moment()],
        'This week': [moment().startOf('week'), moment().endOf('week')],
        'Next week': [moment().add(7, 'days').startOf('week'), moment().add(7, 'days').endOf('week')],
        'This Month': [moment().startOf('month'), moment().endOf('month')],
        'Next Month': [moment().add(1,'month').startOf('month'), moment().add(1,'month').endOf('month')]
    },
    "alwaysShowCalendars": true,
   // "startDate": moment().startOf('week'),
  //  "endDate": moment().add(7, 'days').endOf('week'),
    "opens": "left",
    locale: {
      format: 'DD-MM-YYYY'
    }
}, function(start, end, label) {
	document.getElementById('f').value = start.format('YYYY-MM-DD');
	document.getElementById('t').value = end.format('YYYY-MM-DD');
	document.getElementById('frmDateRange').submit();
  console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
});
})

function planPeriod() {
  var startPlan 	= document.getElementById('f').value;
  var endPlan 		= document.getElementById('t').value;
  
  var planData = {
      f: startPlan,
      t: endPlan,
      
    };
  
 
  console.log('Plan period: ' + startPlan + ' to ' + endPlan);
  console.log('JSON: ' + JSON.stringify(planData));
	$.ajax({
            url: '/api/v1/dayrecipe/planperiod',
            type: 'POST',
            data: planData,
            //contentType: 'json',
            success: function (result) {
                // Do something with the result
                location.reload();
            }
        });
}

