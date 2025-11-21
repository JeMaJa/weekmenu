// Ingredient Management JavaScript
// Handles search, CRUD operations, and UI interactions

let allIngredients = [];
let currentSort = { field: 'name', ascending: true };

// Load ingredients on page load
$(document).ready(function() {
    loadIngredients();
    setupEventHandlers();
});

// Setup all event handlers
function setupEventHandlers() {
    // Search input
    $('#searchInput').on('keyup', function() {
        filterIngredients();
    });
    
    // Clear search
    $('#clearSearch, #clearSearchBtn').on('click', function() {
        $('#searchInput').val('');
        filterIngredients();
    });
    
    // Save ingredient button
    $('#saveIngredientBtn').on('click', function() {
        saveIngredient();
    });
    
    // Confirm delete button
    $('#confirmDeleteBtn').on('click', function() {
        executeDelete();
    });
    
    // Sort headers
    $('.sortable').on('click', function() {
        const field = $(this).data('sort');
        sortIngredients(field);
    });
    
    // Reset form when modal closes
    $('#addIngredientModal').on('hidden.bs.modal', function() {
        resetForm();
    });
}

// Load all ingredients from API
function loadIngredients() {
    $.ajax({
        url: '/api/v1/ingredient/ingredient',
        type: 'GET',
        success: function(data) {
            allIngredients = data;
            displayIngredients(allIngredients);
            updateCounts(allIngredients.length, allIngredients.length);
        },
        error: function(xhr, status, error) {
            showAlert('Error loading ingredients: ' + error, 'danger');
            allIngredients = [];
            displayIngredients([]);
        }
    });
}

// Display ingredients in table
function displayIngredients(ingredients) {
    const tbody = $('#ingredientsTableBody');
    tbody.empty();
    
    if (ingredients.length === 0) {
        // Check if this is due to search or actually no ingredients
        if ($('#searchInput').val().trim() !== '') {
            // No results from search
            $('#ingredientsTable').hide();
            $('#emptyState').hide();
            $('#noResultsState').show();
        } else {
            // Actually no ingredients in database
            $('#ingredientsTable').hide();
            $('#noResultsState').hide();
            $('#emptyState').show();
        }
        return;
    }
    
    // Show table, hide empty states
    $('#ingredientsTable').show();
    $('#emptyState').hide();
    $('#noResultsState').hide();
    
    // Populate table
    ingredients.forEach(function(ingredient) {
        const row = createIngredientRow(ingredient);
        tbody.append(row);
    });
}

// Create table row for an ingredient
function createIngredientRow(ingredient) {
    const uomDisplay = formatUOM(ingredient.uom);
    
    return `
        <tr data-ingredient-id="${ingredient.id}">
            <td class="ingredient-name">${escapeHtml(ingredient.name)}</td>
            <td class="ingredient-uom">${uomDisplay}</td>
            <td>
                <img src="/images/pen.svg" 
                     class="action-icons" 
                     onclick="editIngredient(${ingredient.id})"
                     title="Edit ingredient"
                     style="width: 20px; height: 20px;">
                <img src="/images/trash.svg" 
                     class="action-icons" 
                     onclick="deleteIngredient(${ingredient.id})"
                     title="Delete ingredient"
                     style="width: 20px; height: 20px;">
            </td>
        </tr>
    `;
}

// Filter ingredients based on search input
function filterIngredients() {
    const searchTerm = $('#searchInput').val().toLowerCase().trim();
    
    if (searchTerm === '') {
        // No search - show all
        displayIngredients(allIngredients);
        updateCounts(allIngredients.length, allIngredients.length);
        return;
    }
    
    // Filter by name (case-insensitive)
    const filtered = allIngredients.filter(function(ingredient) {
        return ingredient.name.toLowerCase().includes(searchTerm);
    });
    
    displayIngredients(filtered);
    updateCounts(allIngredients.length, filtered.length);
}

// Sort ingredients
function sortIngredients(field) {
    // Toggle sort direction if clicking same field
    if (currentSort.field === field) {
        currentSort.ascending = !currentSort.ascending;
    } else {
        currentSort.field = field;
        currentSort.ascending = true;
    }
    
    allIngredients.sort(function(a, b) {
        let aVal = field === 'name' ? a.name.toLowerCase() : a.uom;
        let bVal = field === 'name' ? b.name.toLowerCase() : b.uom;
        
        if (aVal < bVal) return currentSort.ascending ? -1 : 1;
        if (aVal > bVal) return currentSort.ascending ? 1 : -1;
        return 0;
    });
    
    filterIngredients(); // Re-display with current filter
}

// Open edit modal with ingredient data
function editIngredient(ingredientId) {
    const ingredient = allIngredients.find(ing => ing.id === ingredientId);
    if (!ingredient) {
        showAlert('Ingredient not found', 'danger');
        return;
    }
    
    // Populate form
    $('#ingredientId').val(ingredient.id);
    $('#ingredientName').val(ingredient.name);
    $('#ingredientUom').val(ingredient.uom);
    $('#isUpdate').val('true');
    
    // Update modal title
    $('#addIngredientModalLabel').text('Edit Ingredient');
    
    // Show modal
    $('#addIngredientModal').modal('show');
}

// Save ingredient (create or update)
function saveIngredient() {
    // Get form values
    const id = $('#ingredientId').val();
    const name = $('#ingredientName').val().trim();
    const uom = $('#ingredientUom').val();
    const isUpdate = $('#isUpdate').val() === 'true';
    
    // Validation
    if (!name || name.length === 0) {
        $('#ingredientName').addClass('is-invalid');
        $('#nameError').text('Ingredient name is required');
        return;
    }
    
    if (!uom) {
        $('#ingredientUom').addClass('is-invalid');
        return;
    }
    
    // Check for duplicates (case-insensitive)
    const duplicate = allIngredients.find(ing => 
        ing.name.toLowerCase() === name.toLowerCase() && 
        ing.id != id // Exclude self when editing
    );
    
    if (duplicate) {
        $('#ingredientName').addClass('is-invalid');
        $('#nameError').text('An ingredient with this name already exists: "' + duplicate.name + '"');
        return;
    }
    
    // Remove validation classes
    $('#ingredientName').removeClass('is-invalid');
    $('#ingredientUom').removeClass('is-invalid');
    
    // Prepare data
    const ingredientData = {
        id: id ? parseInt(id) : 0,
        name: name,
        uom: uom,
        update: isUpdate
    };
    
    // Determine endpoint and method
    const url = isUpdate ? '/api/v1/ingredient/ingredient/' + id : '/api/v1/ingredient/ingredient';
    const method = isUpdate ? 'PUT' : 'POST';
    
    // Save to API
    $.ajax({
        url: url,
        type: method,
        data: JSON.stringify(ingredientData),
        contentType: 'application/json',
        success: function(response) {
            const action = isUpdate ? 'updated' : 'added';
            showAlert('Ingredient ' + action + ' successfully!', 'success');
            $('#addIngredientModal').modal('hide');
            loadIngredients(); // Reload list
        },
        error: function(xhr, status, error) {
            if (xhr.status === 400) {
                showAlert('Invalid ingredient data. Please check your input.', 'danger');
            } else {
                showAlert('Error saving ingredient: ' + error, 'danger');
            }
        }
    });
}

// Delete ingredient
function deleteIngredient(ingredientId) {
    const ingredient = allIngredients.find(ing => ing.id === ingredientId);
    if (!ingredient) {
        showAlert('Ingredient not found', 'danger');
        return;
    }
    
    // Set ingredient name in modal
    $('#deleteIngredientName').text(ingredient.name);
    $('#deleteIngredientId').val(ingredientId);
    
    // Check if ingredient is used in any recipes
    checkIngredientUsage(ingredientId, function(usageCount) {
        if (usageCount > 0) {
            // Show warning - cannot delete
            $('#usageWarning').show();
            $('#usageCount').text(usageCount);
            $('#confirmDeleteBtn').prop('disabled', true).addClass('disabled');
        } else {
            // Can delete
            $('#usageWarning').hide();
            $('#confirmDeleteBtn').prop('disabled', false).removeClass('disabled');
        }
        
        // Show modal
        $('#deleteConfirmModal').modal('show');
    });
}

// Check how many recipes use this ingredient
function checkIngredientUsage(ingredientId, callback) {
    // For now, we'll implement a simple check
    // TODO: Add proper endpoint to get usage count
    
    // Temporary: Return 0 to allow deletion
    // In production, this should call an API endpoint
    callback(0);
    
    /* Future implementation:
    $.ajax({
        url: '/api/v1/ingredient/ingredient/' + ingredientId + '/usage',
        type: 'GET',
        success: function(data) {
            callback(data.count);
        },
        error: function() {
            callback(0); // Assume not used if error
        }
    });
    */
}

// Execute the deletion
function executeDelete() {
    const ingredientId = $('#deleteIngredientId').val();
    
    $.ajax({
        url: '/api/v1/ingredient/ingredient/' + ingredientId,
        type: 'DELETE',
        success: function() {
            showAlert('Ingredient deleted successfully', 'success');
            $('#deleteConfirmModal').modal('hide');
            loadIngredients(); // Reload list
        },
        error: function(xhr, status, error) {
            if (xhr.status === 409) {
                showAlert('Cannot delete - ingredient is used in recipes', 'warning');
            } else {
                showAlert('Error deleting ingredient: ' + error, 'danger');
            }
            $('#deleteConfirmModal').modal('hide');
        }
    });
}

// Reset form to initial state
function resetForm() {
    $('#ingredientForm')[0].reset();
    $('#ingredientId').val('');
    $('#isUpdate').val('false');
    $('#ingredientName').removeClass('is-invalid');
    $('#ingredientUom').removeClass('is-invalid');
    $('#addIngredientModalLabel').text('Add Ingredient');
}

// Update ingredient counts
function updateCounts(total, visible) {
    $('#totalCount').text(total);
    $('#visibleCount').text(visible);
}

// Show alert message
function showAlert(message, type) {
    const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
    
    $('#alertContainer').html(alertHtml);
    
    // Auto-dismiss after 5 seconds
    setTimeout(function() {
        $('.alert').alert('close');
    }, 5000);
}

// Format UOM for display
function formatUOM(uom) {
    const uomMap = {
        'PIECE': 'Piece',
        'GRAM': 'Gram',
        'KG': 'Kilogram',
        'LITER': 'Liter',
        'ML': 'Milliliter',
        'SPOON': 'Spoon',
        'TABLESPOON': 'Tablespoon'
    };
    return uomMap[uom] || uom;
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, function(m) { return map[m]; });
}
