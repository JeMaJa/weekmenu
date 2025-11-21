package nl.jemaja.weekmenu.controller.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nl.jemaja.weekmenu.model.Ingredient;
import nl.jemaja.weekmenu.model.IngredientQuantity;
import nl.jemaja.weekmenu.model.IngredientQuantityKey;
import nl.jemaja.weekmenu.model.Recipe;
import nl.jemaja.weekmenu.model.UOM;
import nl.jemaja.weekmenu.service.IngredientQuantityService;
import nl.jemaja.weekmenu.service.IngredientService;
import nl.jemaja.weekmenu.service.RecipeService;
import nl.jemaja.weekmenu.util.exceptions.NotFoundException;

/**
 * Unit tests for DELETE functionality in IngredientQuantityControllerV1
 * Tests the ability to remove ingredients from recipes
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("IngredientQuantityControllerV1 DELETE Tests")
class IngredientQuantityControllerV1DeleteTest {

    @Mock
    private IngredientQuantityService iQService;

    @Mock
    private IngredientService iService;

    @Mock
    private RecipeService rService;

    @InjectMocks
    private IngredientQuantityControllerV1 controller;

    private Recipe testRecipe;
    private Ingredient testIngredient;
    private IngredientQuantity testIngredientQuantity;

    @BeforeEach
    void setUp() {
        // Setup test recipe
        testRecipe = Recipe.builder()
                .recipeId(1L)
                .recipeName("Test Pizza")
                .description("A delicious test pizza")
                .servings(4)
                .build();

        // Setup test ingredient
        testIngredient = Ingredient.builder()
                .id(1L)
                .name("Flour")
                .uom(UOM.GRAM)
                .build();

        // Setup test IngredientQuantity
        IngredientQuantityKey key = IngredientQuantityKey.builder()
                .ingredientId(1L)
                .recipeId(1L)
                .build();
        
        testIngredientQuantity = IngredientQuantity.builder()
                .id(key)
                .ingredient(testIngredient)
                .recipe(testRecipe)
                .quantity(200.0f)
                .build();
    }

    // ========== DELETE Tests - Happy Path ==========

    @Test
    @DisplayName("DELETE - Should successfully delete ingredient from recipe")
    void deleteIngredientQuantity_WhenExists_ShouldDeleteAndReturnNoContent() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity);

        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        
        // Verify delete was called exactly once
        verify(iQService, times(1)).delete(testIngredientQuantity);
    }

    @Test
    @DisplayName("DELETE - Should delete correct ingredient quantity object")
    void deleteIngredientQuantity_ShouldDeleteCorrectObject() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity);

        // Act
        controller.deleteIngredientQuantity(1L, 1L);

        // Assert - verify the exact object was deleted
        verify(iQService).delete(argThat(iq -> 
            iq.getIngredient().equals(testIngredient) && 
            iq.getRecipe().equals(testRecipe) &&
            iq.getQuantity() == 200.0f
        ));
    }

    // ========== DELETE Tests - Not Found Scenarios ==========

    @Test
    @DisplayName("DELETE - Should return NOT_FOUND when ingredient not in recipe")
    void deleteIngredientQuantity_WhenNotInRecipe_ShouldReturnNotFound() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(null); // Ingredient not in recipe

        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        // Verify delete was never called
        verify(iQService, never()).delete(any());
    }

    @Test
    @DisplayName("DELETE - Should return NOT_FOUND when ingredient doesn't exist")
    void deleteIngredientQuantity_WhenIngredientNotFound_ShouldReturnNotFound() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenThrow(new NotFoundException("Ingredient not found"));

        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(iQService, never()).delete(any());
    }

    @Test
    @DisplayName("DELETE - Should return NOT_FOUND when recipe doesn't exist")
    void deleteIngredientQuantity_WhenRecipeNotFound_ShouldReturnNotFound() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenThrow(new NotFoundException("Recipe not found"));

        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(iQService, never()).delete(any());
    }

    // ========== DELETE Tests - Validation ==========

    @Test
    @DisplayName("DELETE - Should reject invalid ingredient ID (zero)")
    void deleteIngredientQuantity_WithZeroIngredientId_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 0L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("DELETE - Should reject invalid ingredient ID (negative)")
    void deleteIngredientQuantity_WithNegativeIngredientId_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, -1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("DELETE - Should reject invalid recipe ID (zero)")
    void deleteIngredientQuantity_WithZeroRecipeId_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(0L, 1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("DELETE - Should reject invalid recipe ID (negative)")
    void deleteIngredientQuantity_WithNegativeRecipeId_ShouldReturnBadRequest() {
        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(-1L, 1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("DELETE - Should reject both IDs invalid")
    void deleteIngredientQuantity_WithBothIdsInvalid_ShouldReturnBadRequest() {
        // Test various combinations of invalid IDs
        long[][] invalidCombinations = {{0L, 0L}, {-1L, -1L}, {0L, -1L}, {-1L, 0L}};
        
        for (long[] ids : invalidCombinations) {
            // Act
            ResponseEntity<Void> response = controller.deleteIngredientQuantity(ids[0], ids[1]);
            
            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                    "Should reject recipeId=" + ids[0] + ", ingredientId=" + ids[1]);
        }
    }

    // ========== DELETE Tests - Error Handling ==========

    @Test
    @DisplayName("DELETE - Should return INTERNAL_SERVER_ERROR on delete failure")
    void deleteIngredientQuantity_WhenDeleteFails_ShouldReturnServerError() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity);
        doThrow(new RuntimeException("Database error"))
                .when(iQService).delete(testIngredientQuantity);

        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("DELETE - Should return INTERNAL_SERVER_ERROR on unexpected exception")
    void deleteIngredientQuantity_WhenUnexpectedException_ShouldReturnServerError() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(any(), any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 1L);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ========== DELETE Tests - Multiple Ingredients ==========

    @Test
    @DisplayName("DELETE - Should delete only specified ingredient from recipe with multiple ingredients")
    void deleteIngredientQuantity_WithMultipleIngredients_ShouldDeleteOnlySpecified() throws NotFoundException {
        // Arrange - Create another ingredient
        Ingredient ingredient2 = Ingredient.builder()
                .id(2L)
                .name("Water")
                .uom(UOM.ML)
                .build();
        
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity);

        // Act - Delete first ingredient
        controller.deleteIngredientQuantity(1L, 1L);

        // Assert - Only flour was deleted, not water
        verify(iQService, times(1)).delete(testIngredientQuantity);
        verify(iQService, never()).delete(argThat(iq -> 
            iq.getIngredient().equals(ingredient2)
        ));
    }

    // ========== DELETE Tests - Integration Scenarios ==========

    @Test
    @DisplayName("DELETE - Should handle delete after create workflow")
    void deleteIngredientQuantity_AfterCreate_ShouldWork() throws NotFoundException {
        // Arrange - Simulate ingredient was just created
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity);

        // Act - Delete immediately after creation
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 1L);

        // Assert - Should work fine
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(iQService, times(1)).delete(testIngredientQuantity);
    }

    @Test
    @DisplayName("DELETE - Should return NOT_FOUND when deleting already deleted ingredient")
    void deleteIngredientQuantity_WhenAlreadyDeleted_ShouldReturnNotFound() throws NotFoundException {
        // Arrange - Ingredient was already deleted
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(null); // Already deleted

        // Act - Try to delete again
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 1L);

        // Assert - Should return NOT_FOUND (idempotent operation)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(iQService, never()).delete(any());
    }

    // ========== DELETE Tests - Edge Cases ==========

    @Test
    @DisplayName("DELETE - Should handle deletion of ingredient with large quantity")
    void deleteIngredientQuantity_WithLargeQuantity_ShouldWork() throws NotFoundException {
        // Arrange - Large quantity
        testIngredientQuantity.setQuantity(999999.99f);
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity);

        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 1L);

        // Assert - Quantity doesn't matter for deletion
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(iQService, times(1)).delete(testIngredientQuantity);
    }

    @Test
    @DisplayName("DELETE - Should handle deletion of ingredient with fractional quantity")
    void deleteIngredientQuantity_WithFractionalQuantity_ShouldWork() throws NotFoundException {
        // Arrange - Fractional quantity
        testIngredientQuantity.setQuantity(0.5f);
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity);

        // Act
        ResponseEntity<Void> response = controller.deleteIngredientQuantity(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(iQService, times(1)).delete(testIngredientQuantity);
    }
}
