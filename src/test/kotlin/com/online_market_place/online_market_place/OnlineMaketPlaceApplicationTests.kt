package com.online_market_place.online_market_place

import com.online_market_place.online_market_place.test_config.TestConfig
import mu.KotlinLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest(
    classes = [OnlineMarketPlaceApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import(TestConfig::class)
@ActiveProfiles("test")
@Testcontainers
class OnlineMarketPlaceApplicationTests {

    private val logger = KotlinLogging.logger {}

    // Injected PostgresSQL and RabbitMQ containers
    @Autowired
    lateinit var postgresContainer: PostgreSQLContainer<*>

    @Autowired
    lateinit var rabbitmqContainer: RabbitMQContainer

    @Autowired
    lateinit var mailhogContainer: GenericContainer<*>

    @BeforeEach
    fun setUp() {
        if (!postgresContainer.isRunning) {
            postgresContainer.start()
        }

        // Removed MailHog check
    }

    @Test
    fun `test postgres container is running`() {
        // Verify that the PostgresSQL container is running
        assert(postgresContainer.isRunning)
        logger.info { "PostgresSQL container is running: ${postgresContainer.isRunning}" }
        logger.info { "PostgresSQL container URL: ${postgresContainer.jdbcUrl}" }
        logger.info { "PostgresSQL container username: ${postgresContainer.username}" }
        logger.info { "PostgresSQL container password: ${postgresContainer.password}" }
    }

    @Test
    fun `test rabbitmq container is running`() {
        // Verify that the RabbitMQ container is running
        assert(rabbitmqContainer.isRunning)
        logger.info { "RabbitMQ container is running: ${rabbitmqContainer.isRunning}" }
    }

    @Test
    fun `test mailhog container is running`() {
        // Verify that the MailHog container is running
        assert(mailhogContainer.isRunning)
        logger.info { "MailHog container is running: ${mailhogContainer.isRunning}" }
        logger.info { "MailHog container SMTP port: ${mailhogContainer.getMappedPort(1025)}" }
    }
}
