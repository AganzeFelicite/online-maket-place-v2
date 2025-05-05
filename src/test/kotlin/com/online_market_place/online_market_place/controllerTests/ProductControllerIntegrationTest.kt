package com.online_market_place.online_market_place.controllerTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.online_market_place.online_market_place.category.dto.CreateCategoryDTO
import com.online_market_place.online_market_place.category.entities.CategoryEntity
import com.online_market_place.online_market_place.category.repositories.CategoryRepository
import com.online_market_place.online_market_place.category.services.CategoryService
import com.online_market_place.online_market_place.product.dto.CreateProductDTO

import com.online_market_place.online_market_place.product.dto.UpdateProductDTO
import com.online_market_place.online_market_place.product.mappers.ProductMapper

import com.online_market_place.online_market_place.product.repositories.ProductRepository
import com.online_market_place.online_market_place.test_config.TestConfig
import mu.KotlinLogging
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


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestConfig::class)
class ProductControllerIntegrationTest {

    val log = KotlinLogging.logger {}

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var categoryService: CategoryService

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setup() {
        productRepository.deleteAllPhysically()
        categoryRepository.deleteAllPhysically()
        categoryService.createCategory(CreateCategoryDTO.Input(name = "Electronics"))
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should create product and return 201`() {
        val category = categoryRepository.save(CategoryEntity(name = "Foods"))
        log.info { "Created category with ID: ${category.id}" }
        val request = CreateProductDTO.Input(
            name = "Test Product",
            description = "Delicious food",
            price = 20.5,
            categoryId = category.id,
            stockQuantity = 10,
            featured = true,
            imageUrl = null
        )

        val result = mockMvc.perform(
            post("/api/v2.0/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andReturn()
        val response = result.response
        assertEquals(HttpStatus.CREATED.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `get all products should return 200`() {
        val result = mockMvc.perform(
            get("/api/v2.0/products")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray)
            .andReturn()

        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should get product by ID and return 200`() {
        // Setup: Create a product for testing
        val category = categoryRepository.save(CategoryEntity(name = "Foods"))
        log.info { "Created category with ID: ${category.id}" }

        val product = CreateProductDTO.Input(
            name = "Test Product",
            description = "A great product",
            price = 20.0,
            categoryId = category.id,
            stockQuantity = 100,
            featured = true
        )
        val savedProduct =
            productRepository.save(ProductMapper().map(product, categoryRepository.findById(product.categoryId).get()))
        log.info { "Saved product: $savedProduct" }

        val result = mockMvc.perform(
            get("/api/v2.0/products/${savedProduct.id}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test Product"))
            .andExpect(jsonPath("$.description").value("A great product"))
            .andExpect(jsonPath("$.price").value(20.0))
            .andExpect(jsonPath("$.stockQuantity").value(100))
            .andReturn()

        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should update product and return 200`() {
        val category = categoryRepository.save(CategoryEntity(name = "Foods"))
        log.info { "Created category with ID: ${category.id}" }

        val product = CreateProductDTO.Input(
            name = "Old Product",
            description = "Old description",
            price = 10.0,
            categoryId = category.id,
            stockQuantity = 20,
            featured = false,
            imageUrl = null
        )
        val savedProduct =
            productRepository.save(ProductMapper().map(product, categoryRepository.findById(product.categoryId).get()))

        val updatedRequest = UpdateProductDTO.Input(
            name = "Updated Product",
            description = "Updated description",
            price = 15.0,
            stockQuantity = 30,
            featured = true,
            imageUrl = "http://example.com/new-image.jpg",
            categoryId = category.id
        )

        // Step 3: Make the PUT request to update the product
        val result = mockMvc.perform(
            put("/api/v2.0/products/${savedProduct.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Product"))
            .andExpect(jsonPath("$.description").value("Updated description"))
            .andExpect(jsonPath("$.price").value(15.0))
            .andExpect(jsonPath("$.stockQuantity").value(30))
            .andExpect(jsonPath("$.featured").value(true))
            .andExpect(jsonPath("$.imageUrl").value("http://example.com/new-image.jpg"))
            .andReturn()

        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should delete product and return 200`() {
        val category = categoryRepository.save(CategoryEntity(name = "Foods"))
        log.info { "Created category with ID: ${category.id}" }
        // Step 1: Create a product to delete
        val product = CreateProductDTO.Input(
            name = "Product to Delete",
            description = "This product will be deleted",
            price = 10.0,
            categoryId = category.id,
            stockQuantity = 20,
            featured = false,
            imageUrl = null
        )
        val savedProduct =
            productRepository.save(ProductMapper().map(product, categoryRepository.findById(product.categoryId).get()))

        // Step 2: Perform the DELETE request to delete the product
        val result = mockMvc.perform(
            delete("/api/v2.0/products/${savedProduct.id}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("Product with ID ${savedProduct.id} deleted successfully"))
            .andExpect(jsonPath("$.success").value(true))
            .andReturn()

        val response = result.response
        assertEquals(HttpStatus.OK.value(), response.status)
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.contentType)

        val deletedProduct = productRepository.findById(savedProduct.id)
        assert(deletedProduct.isEmpty)
    }
}