package com.online_market_place.online_market_place.controllerTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.online_market_place.online_market_place.category.dto.CreateCategoryRequest
import com.online_market_place.online_market_place.category.dto.ProductCategoryRepository
import com.online_market_place.online_market_place.category.entity.CategoryEntity
import com.online_market_place.online_market_place.category.service.CategoryService
import com.online_market_place.online_market_place.product.dto.CreateProductRequest
import com.online_market_place.online_market_place.product.dto.UpdateProductRequest
import com.online_market_place.online_market_place.product.repository.ProductRepository
import com.online_market_place.online_market_place.product.toProductEntity
import mu.KotlinLogging
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
class ProductControllerIntegrationTest {

    val log = KotlinLogging.logger {}

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var categoryService: CategoryService

    @Autowired
    private lateinit var categoryRepository: ProductCategoryRepository




    @Autowired
    lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setup() {
        productRepository.deleteAll()
        categoryRepository.deleteAll()
        categoryService.createCategory(CreateCategoryRequest(name = "Foods"))
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should create product and return 201`() {
        val category = categoryRepository.save(CategoryEntity(name = "Foods"))
        log.info { "Created category with ID: ${category.id}" }
        val request = CreateProductRequest(
            name = "Test Product",
            description = "Delicious food",
            price = 20.5,
            categoryId = category.id,
            stockQuantity = 10,
            featured = true,
            imageUrl = null
        )

        mockMvc.perform(post("/api/v2.0/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated)

    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `get all products should return 200`() {

        mockMvc.perform(get("/api/v2.0/products")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())  //
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should get product by ID and return 200`() {
        // Setup: Create a product for testing
        val category = categoryRepository.save(CategoryEntity(name = "Foods"))
        log.info { "Created category with ID: ${category.id}" }

        val product = CreateProductRequest(
            name = "Test Product",
            description = "A great product",
            price = 20.0,
            categoryId = category.id,
            stockQuantity = 100,
            featured = true
        )
        val savedProduct = productRepository.save(product.toProductEntity(categoryRepository.findById(product.categoryId).get()))
        log.info { "Saved product: $savedProduct" }
        mockMvc.perform(get("/api/v2.0/products/${savedProduct.id}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test Product"))
            .andExpect(jsonPath("$.description").value("A great product"))
            .andExpect(jsonPath("$.price").value(20.0))
            .andExpect(jsonPath("$.stockQuantity").value(100))
    }

    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should update product and return 200`() {

        val category = categoryRepository.save(CategoryEntity(name = "Foods"))
        log.info { "Created category with ID: ${category.id}" }

        val product = CreateProductRequest(
            name = "Old Product",
            description = "Old description",
            price = 10.0,
            categoryId = category.id,
            stockQuantity = 20,
            featured = false,
            imageUrl = null
        )
        val savedProduct = productRepository.save(product.toProductEntity(categoryRepository.findById(product.categoryId).get()))



        val updatedRequest = UpdateProductRequest(
            name = "Updated Product",
            description = "Updated description",
            price = 15.0,
            stockQuantity = 30,
            featured = true,
            imageUrl = "http://example.com/new-image.jpg",
            categoryId = category.id

        )

        // Step 3: Make the PUT request to update the product
        mockMvc.perform(put("/api/v2.0/products/${savedProduct.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedRequest)))
            .andExpect(status().isOk)  // Expecting HTTP 200
            .andExpect(jsonPath("$.name").value("Updated Product"))
            .andExpect(jsonPath("$.description").value("Updated description"))
            .andExpect(jsonPath("$.price").value(15.0))
            .andExpect(jsonPath("$.stockQuantity").value(30))
            .andExpect(jsonPath("$.featured").value(true))
            .andExpect(jsonPath("$.imageUrl").value("http://example.com/new-image.jpg"))
    }


    @Test
    @WithMockUser(roles = ["SELLER"])
    fun `should delete product and return 200`() {
        val category = categoryRepository.save(CategoryEntity(name = "Foods"))
        log.info { "Created category with ID: ${category.id}" }
        // Step 1: Create a product to delete
        val product = CreateProductRequest(
            name = "Product to Delete",
            description = "This product will be deleted",
            price = 10.0,
            categoryId = category.id,
            stockQuantity = 20,
            featured = false,
            imageUrl = null
        )
        val savedProduct = productRepository.save(product.toProductEntity(categoryRepository.findById(product.categoryId).get()))

        // Step 2: Perform the DELETE request to delete the product
        mockMvc.perform(delete("/api/v2.0/products/${savedProduct.id}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)  // Expecting HTTP 200
            .andExpect(jsonPath("$.message").value("Product with ID ${savedProduct.id} deleted successfully"))
            .andExpect(jsonPath("$.success").value(true))

        // Step 3: Verify the product is deleted (optional, if your repository allows direct queries)
        val deletedProduct = productRepository.findById(savedProduct.id)
        assert(deletedProduct.isEmpty)
    }

}
