package com.online_market_place.online_market_place.controllerTests
import com.fasterxml.jackson.databind.ObjectMapper
import com.online_market_place.online_market_place.category.dto.CreateCategoryDTO
import com.online_market_place.online_market_place.category.dto.UpdateCategoryDTO
import com.online_market_place.online_market_place.category.mappers.CategoryMapper
import com.online_market_place.online_market_place.category.repositories.CategoryRepository
import com.online_market_place.online_market_place.test_config.TestConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestConfig::class)
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
        categoryRepository.deleteAllPhysically()
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should get all categories`() {


        val result = mockMvc.perform(get("/api/v2.0/categories"))
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
        assertEquals("[]", response.contentAsString)

    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should get category by id`() {
        val createCategoryRequest = CreateCategoryDTO.Input(name = "Electronics")
        val category = categoryRepository.save(CategoryMapper().map(createCategoryRequest))

        val result = mockMvc.perform(get("/api/v2.0/categories/${category.id}"))
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
        assertEquals(
            """{"id":${category.id},"name":"Electronics","products":[]}""",
            response.contentAsString
        )
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should create new category`() {
        val createCategoryRequest = CreateCategoryDTO.Input(name = "Electronics")

        val result = mockMvc.perform(
            post("/api/v2.0/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCategoryRequest))
        ).andReturn()
        val response = result.response
        assertEquals(HttpStatus.CREATED.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)


    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should update category`() {
        val createCategoryRequest = CreateCategoryDTO.Input(name = "Electronics")
        val category = categoryRepository.save(CategoryMapper().map(createCategoryRequest))
        val updateCategoryRequest = UpdateCategoryDTO.Input(id = category.id, name = "Updated Electronics")

        val result = mockMvc.perform(
            put("/api/v2.0/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCategoryRequest))
        )
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
        assertEquals(
            """{"id":${category.id},"name":"Updated Electronics"}""",
            response.contentAsString
        )

    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should delete category`() {
        val createCategoryRequest = CreateCategoryDTO.Input(name = "Electronics")
        val category = categoryRepository.save(CategoryMapper().map(createCategoryRequest))

        val result = mockMvc.perform(delete("/api/v2.0/categories/${category.id}"))
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(true, response.contentAsString.contains("Category with ID ${category.id} deleted successfully"))
        assertEquals(true, response.contentAsString.contains("success"))
    }
}

