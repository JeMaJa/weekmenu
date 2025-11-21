package nl.jemaja.weekmenu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.jemaja.weekmenu.model.Ingredient;
import nl.jemaja.weekmenu.model.UOM;
import nl.jemaja.weekmenu.repository.IngredientRepository;

/**
 * Unit tests for IngredientService - Case-Insensitive Duplicate Detection
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("IngredientService - Duplicate Detection Tests")
class IngredientServiceDuplicateTest {

    @Mock
    private IngredientRepository ingredientRepo;

    @InjectMocks
    private IngredientService ingredientService;

    private Ingredient flourIngredient;

    @BeforeEach
    void setUp() {
        flourIngredient = Ingredient.builder()
                .id(1L)
                .name("Flour")
                .uom(UOM.GRAM)
                .build();
    }

    // ========== existsByNameIgnoreCase Tests ==========

    @Test
    @DisplayName("Should find existing ingredient with exact match")
    void existsByNameIgnoreCase_ExactMatch_ShouldReturnTrue() {
        // Arrange
        when(ingredientRepo.findByNameIgnoreCase("Flour"))
                .thenReturn(Arrays.asList(flourIngredient));

        // Act
        boolean exists = ingredientService.existsByNameIgnoreCase("Flour");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should find existing ingredient with lowercase input")
    void existsByNameIgnoreCase_Lowercase_ShouldReturnTrue() {
        // Arrange
        when(ingredientRepo.findByNameIgnoreCase("flour"))
                .thenReturn(Arrays.asList(flourIngredient));

        // Act
        boolean exists = ingredientService.existsByNameIgnoreCase("flour");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should find existing ingredient with uppercase input")
    void existsByNameIgnoreCase_Uppercase_ShouldReturnTrue() {
        // Arrange
        when(ingredientRepo.findByNameIgnoreCase("FLOUR"))
                .thenReturn(Arrays.asList(flourIngredient));

        // Act
        boolean exists = ingredientService.existsByNameIgnoreCase("FLOUR");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should find existing ingredient with mixed case input")
    void existsByNameIgnoreCase_MixedCase_ShouldReturnTrue() {
        // Arrange
        when(ingredientRepo.findByNameIgnoreCase("FlOuR"))
                .thenReturn(Arrays.asList(flourIngredient));

        // Act
        boolean exists = ingredientService.existsByNameIgnoreCase("FlOuR");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when ingredient doesn't exist")
    void existsByNameIgnoreCase_NotFound_ShouldReturnFalse() {
        // Arrange
        when(ingredientRepo.findByNameIgnoreCase("Sugar"))
                .thenReturn(Collections.emptyList());

        // Act
        boolean exists = ingredientService.existsByNameIgnoreCase("Sugar");

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should handle empty string")
    void existsByNameIgnoreCase_EmptyString_ShouldReturnFalse() {
        // Arrange
        when(ingredientRepo.findByNameIgnoreCase(""))
                .thenReturn(Collections.emptyList());

        // Act
        boolean exists = ingredientService.existsByNameIgnoreCase("");

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should handle whitespace")
    void existsByNameIgnoreCase_Whitespace_ShouldReturnFalse() {
        // Arrange
        when(ingredientRepo.findByNameIgnoreCase("   "))
                .thenReturn(Collections.emptyList());

        // Act
        boolean exists = ingredientService.existsByNameIgnoreCase("   ");

        // Assert
        assertFalse(exists);
    }

    // ========== findByNameIgnoreCase Tests ==========

    @Test
    @DisplayName("Should find ingredients by name (case-insensitive)")
    void findByNameIgnoreCase_ShouldReturnMatchingIngredients() {
        // Arrange
        when(ingredientRepo.findByNameIgnoreCase("flour"))
                .thenReturn(Arrays.asList(flourIngredient));

        // Act
        List<Ingredient> result = ingredientService.findByNameIgnoreCase("flour");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Flour", result.get(0).getName());
    }

    @Test
    @DisplayName("Should return empty list when no matches")
    void findByNameIgnoreCase_NoMatch_ShouldReturnEmptyList() {
        // Arrange
        when(ingredientRepo.findByNameIgnoreCase("Sugar"))
                .thenReturn(Collections.emptyList());

        // Act
        List<Ingredient> result = ingredientService.findByNameIgnoreCase("Sugar");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ========== Duplicate Prevention Scenarios ==========

    @Test
    @DisplayName("Should detect duplicate: 'Flour' when 'flour' exists")
    void duplicateDetection_DifferentCase_ShouldDetect() {
        // Arrange - "flour" already exists
        Ingredient existingFlour = Ingredient.builder()
                .id(1L)
                .name("flour")
                .uom(UOM.GRAM)
                .build();
        
        when(ingredientRepo.findByNameIgnoreCase("Flour"))
                .thenReturn(Arrays.asList(existingFlour));

        // Act - Try to add "Flour"
        boolean exists = ingredientService.existsByNameIgnoreCase("Flour");

        // Assert - Should detect duplicate
        assertTrue(exists, "Should detect 'Flour' as duplicate of 'flour'");
    }

    @Test
    @DisplayName("Should detect duplicate: 'SALT' when 'Salt' exists")
    void duplicateDetection_AllCaps_ShouldDetect() {
        // Arrange
        Ingredient existingSalt = Ingredient.builder()
                .id(2L)
                .name("Salt")
                .uom(UOM.GRAM)
                .build();
        
        when(ingredientRepo.findByNameIgnoreCase("SALT"))
                .thenReturn(Arrays.asList(existingSalt));

        // Act
        boolean exists = ingredientService.existsByNameIgnoreCase("SALT");

        // Assert
        assertTrue(exists, "Should detect 'SALT' as duplicate of 'Salt'");
    }

    @Test
    @DisplayName("Should allow different ingredients with similar names")
    void duplicateDetection_DifferentIngredients_ShouldAllowBoth() {
        // Arrange
        when(ingredientRepo.findByNameIgnoreCase("All-purpose flour"))
                .thenReturn(Collections.emptyList());

        // Act - "Flour" exists but "All-purpose flour" doesn't
        boolean exists = ingredientService.existsByNameIgnoreCase("All-purpose flour");

        // Assert - Should allow it (different ingredient)
        assertFalse(exists);
    }
}
