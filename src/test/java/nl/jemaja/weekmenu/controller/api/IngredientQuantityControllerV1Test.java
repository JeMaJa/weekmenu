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

import nl.jemaja.weekmenu.dto.IQDto;
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
 * Unit tests for IngredientQuantityControllerV1
 * Tests the add/update functionality for ingredient quantities in recipes
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("IngredientQuantityControllerV1 Tests")
class IngredientQuantityControllerV1Test {

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
    private IQDto testIQDto;
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

        // Setup test DTO
        testIQDto = IQDto.builder()
                .ingredientId(1L)
                .recipeId(1L)
                .quantity(200.0f)
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

    // ========== GET Tests ==========

    @Test
    @DisplayName("GET - Should return ingredient quantity when found")
    void getIngredient_WhenFound_ShouldReturnOk() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity);

        // Act
        ResponseEntity<IngredientQuantity> response = controller.getIngredient(1L, 1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200.0f, response.getBody().getQuantity());
        assertEquals("Flour", response.getBody().getIngredient().getName());
    }

    @Test
    @DisplayName("GET - Should return NOT_FOUND when ingredient quantity doesn't exist")
    void getIngredient_WhenNotFound_ShouldReturnNotFound() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(null);

        // Act
        ResponseEntity<IngredientQuantity> response = controller.getIngredient(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("GET - Should return NOT_FOUND when ingredient doesn't exist")
    void getIngredient_WhenIngredientNotFound_ShouldReturnNotFound() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenThrow(new NotFoundException("Ingredient not found"));

        // Act
        ResponseEntity<IngredientQuantity> response = controller.getIngredient(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("GET - Should return NOT_FOUND when recipe doesn't exist")
    void getIngredient_WhenRecipeNotFound_ShouldReturnNotFound() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenThrow(new NotFoundException("Recipe not found"));

        // Act
        ResponseEntity<IngredientQuantity> response = controller.getIngredient(1L, 1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ========== POST Tests - CREATE New Ingredient ==========

    @Test
    @DisplayName("POST - Should create new ingredient quantity when it doesn't exist")
    void addIngredientQuantity_WhenNew_ShouldCreateAndReturnCreated() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(null); // Doesn't exist yet

        // Act
        ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200.0f, response.getBody().getQuantity());
        
        // Verify save was called
        verify(iQService, times(1)).save(any(IngredientQuantity.class));
    }

    @Test
    @DisplayName("POST - Should create with different quantity values")
    void addIngredientQuantity_WithDifferentQuantities_ShouldCreate() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(null);

        // Test different valid quantities
        float[] testQuantities = {0.5f, 1.0f, 100.0f, 1000.5f, 9999.99f};
        
        for (float quantity : testQuantities) {
            testIQDto.setQuantity(quantity);
            
            // Act
            ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);
            
            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(quantity, response.getBody().getQuantity());
        }
        
        verify(iQService, times(testQuantities.length)).save(any(IngredientQuantity.class));
    }

    // ========== POST Tests - UPDATE Existing Ingredient ==========

    @Test
    @DisplayName("POST - Should update quantity when ingredient already exists")
    void addIngredientQuantity_WhenExists_ShouldUpdateAndReturnOk() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity); // Already exists
        
        // Change quantity from 200 to 300
        testIQDto.setQuantity(300.0f);

        // Act
        ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(300.0f, response.getBody().getQuantity());
        
        // Verify the existing object was updated and saved
        assertEquals(300.0f, testIngredientQuantity.getQuantity());
        verify(iQService, times(1)).save(testIngredientQuantity);
    }

    @Test
    @DisplayName("POST - Should handle multiple updates correctly")
    void addIngredientQuantity_MultipleUpdates_ShouldUpdateEachTime() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity);

        // Simulate multiple updates
        float[] updates = {250.0f, 300.0f, 150.0f, 500.0f};
        
        for (float newQuantity : updates) {
            testIQDto.setQuantity(newQuantity);
            
            // Act
            ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);
            
            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(newQuantity, testIngredientQuantity.getQuantity());
        }
        
        verify(iQService, times(updates.length)).save(testIngredientQuantity);
    }

    // ========== POST Tests - Validation ==========

    @Test
    @DisplayName("POST - Should reject zero quantity")
    void addIngredientQuantity_WithZeroQuantity_ShouldReturnBadRequest() {
        // Arrange
        testIQDto.setQuantity(0.0f);

        // Act
        ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("POST - Should reject negative quantity")
    void addIngredientQuantity_WithNegativeQuantity_ShouldReturnBadRequest() {
        // Arrange
        testIQDto.setQuantity(-50.0f);

        // Act
        ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("POST - Should reject various invalid quantities")
    void addIngredientQuantity_WithVariousInvalidQuantities_ShouldReturnBadRequest() {
        float[] invalidQuantities = {-100.0f, -1.0f, -0.001f, 0.0f};
        
        for (float invalidQuantity : invalidQuantities) {
            testIQDto.setQuantity(invalidQuantity);
            
            // Act
            ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);
            
            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), 
                    "Should reject quantity: " + invalidQuantity);
        }
    }

    @Test
    @DisplayName("POST - Should reject invalid ingredient ID")
    void addIngredientQuantity_WithInvalidIngredientId_ShouldReturnBadRequest() {
        // Test zero and negative IDs
        long[] invalidIds = {0L, -1L, -100L};
        
        for (long invalidId : invalidIds) {
            testIQDto.setIngredientId(invalidId);
            testIQDto.setRecipeId(1L); // Reset recipe ID to valid value
            
            // Act
            ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);
            
            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                    "Should reject ingredient ID: " + invalidId);
        }
    }

    @Test
    @DisplayName("POST - Should reject invalid recipe ID")
    void addIngredientQuantity_WithInvalidRecipeId_ShouldReturnBadRequest() {
        // Test zero and negative IDs
        long[] invalidIds = {0L, -1L, -100L};
        
        for (long invalidId : invalidIds) {
            testIQDto.setIngredientId(1L); // Reset ingredient ID to valid value
            testIQDto.setRecipeId(invalidId);
            
            // Act
            ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);
            
            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                    "Should reject recipe ID: " + invalidId);
        }
    }

    // ========== POST Tests - Error Handling ==========

    @Test
    @DisplayName("POST - Should return NOT_FOUND when ingredient doesn't exist")
    void addIngredientQuantity_WhenIngredientNotFound_ShouldReturnNotFound() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenThrow(new NotFoundException("Ingredient not found"));

        // Act
        ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("POST - Should return NOT_FOUND when recipe doesn't exist")
    void addIngredientQuantity_WhenRecipeNotFound_ShouldReturnNotFound() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenThrow(new NotFoundException("Recipe not found"));

        // Act
        ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("POST - Should return INTERNAL_SERVER_ERROR on unexpected exception")
    void addIngredientQuantity_WhenUnexpectedException_ShouldReturnServerError() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(any(), any()))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("POST - Should return INTERNAL_SERVER_ERROR when save fails")
    void addIngredientQuantity_WhenSaveFails_ShouldReturnServerError() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(null);
        doThrow(new RuntimeException("Database connection failed"))
                .when(iQService).save(any(IngredientQuantity.class));

        // Act
        ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ========== Integration-style Tests ==========

    @Test
    @DisplayName("POST - Should handle complete workflow: create then update")
    void addIngredientQuantity_CreateThenUpdate_ShouldWorkCorrectly() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        
        // First call - ingredient doesn't exist
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(null);

        // Act - Create
        ResponseEntity<IQDto> createResponse = controller.addIngredientQuantity(testIQDto);

        // Assert - Create
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertEquals(200.0f, createResponse.getBody().getQuantity());
        
        // Now simulate ingredient exists for update
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(testIngredientQuantity);
        
        testIQDto.setQuantity(350.0f);

        // Act - Update
        ResponseEntity<IQDto> updateResponse = controller.addIngredientQuantity(testIQDto);

        // Assert - Update
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(350.0f, updateResponse.getBody().getQuantity());
        
        // Verify interactions
        verify(iQService, times(2)).save(any(IngredientQuantity.class));
    }

    @Test
    @DisplayName("POST - Should handle fractional quantities correctly")
    void addIngredientQuantity_WithFractionalQuantities_ShouldHandleCorrectly() throws NotFoundException {
        // Arrange
        when(iService.findById(1L)).thenReturn(testIngredient);
        when(rService.findByRecipeId(1L)).thenReturn(testRecipe);
        when(iQService.findByRecipeAndIngredient(testRecipe, testIngredient))
                .thenReturn(null);

        // Test various fractional quantities
        float[] fractionalQuantities = {0.5f, 1.5f, 2.25f, 3.333f, 100.125f};
        
        for (float quantity : fractionalQuantities) {
            testIQDto.setQuantity(quantity);
            
            // Act
            ResponseEntity<IQDto> response = controller.addIngredientQuantity(testIQDto);
            
            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(quantity, response.getBody().getQuantity(), 0.001f);
        }
    }
}
