package com.online_market_place.online_market_place.controllerTests
import com.fasterxml.jackson.databind.ObjectMapper
import com.online_market_place.online_market_place.category.dto.CreateCategoryDTO
import com.online_market_place.online_market_place.category.dto.UpdateCategoryDTO
import com.online_market_place.online_market_place.category.mappers.CategoryMapper
import com.online_market_place.online_market_place.category.repositories.CategoryRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @BeforeEach
    fun setup() {
        // Clear DB to avoid conflict
        categoryRepository.deleteAll()
    }
        @Test
        @WithMockUser(roles = ["SELLER"])
        fun `should get all categories`() {


            mockMvc.perform(get("/api/v2.0/categories"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray)

        }

        @Test
        @WithMockUser(roles = ["ADMIN"])
        fun `should get category by id`() {
            val createCategoryRequest = CreateCategoryDTO.Input(name = "Electronics")
            val category = categoryRepository.save(CategoryMapper().map(createCategoryRequest))

            mockMvc.perform(get("/api/v2.0/categories/${category.id}"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(category.id))
                .andExpect(jsonPath("$.name").value("Electronics"))
        }

        @Test
        @WithMockUser(roles = ["SELLER"])
        fun `should create new category`() {
            val createCategoryRequest = CreateCategoryDTO.Input(name = "Electronics")

            mockMvc.perform(
                post("/api/v2.0/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createCategoryRequest))
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$").isMap)

        }

        @Test
        @WithMockUser(roles = ["SELLER"])
        fun `should update category`() {
            val createCategoryRequest = CreateCategoryDTO.Input(name = "Electronics")
            val category = categoryRepository.save(CategoryMapper().map(createCategoryRequest))
            val updateCategoryRequest = UpdateCategoryDTO.Input(id = category.id, name = "Updated Electronics")

            mockMvc.perform(
                put("/api/v2.0/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateCategoryRequest))
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name").value("Updated Electronics"))

        }

        @Test
        @WithMockUser(roles = ["SELLER"])
        fun `should delete category`() {
            val createCategoryRequest = CreateCategoryDTO.Input(name = "Electronics")
            val category = categoryRepository.save(CategoryMapper().map(createCategoryRequest))

            mockMvc.perform(delete("/api/v2.0/categories/${category.id}"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.message").value("Category with ID ${category.id} deleted successfully"))
        }
    }

