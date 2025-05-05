package com.online_market_place.online_market_place.test_config


import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestConfig {
    companion object {
        private val postgres = PostgreSQLContainer(DockerImageName.parse("postgres:15")).withReuse(true)
        private val rabbitmq = RabbitMQContainer(DockerImageName.parse("rabbitmq:3.11-management")).withReuse(true)

        // MailHog setup
        private val mailhog = GenericContainer(DockerImageName.parse("mailhog/mailhog:latest"))
            .withExposedPorts(1025, 8025)  // SMTP port and Web UI port
            .withReuse(true)
            .apply { start() }

        @Bean
        @ServiceConnection
        fun postgresContainer(): PostgreSQLContainer<*> = postgres

        @Bean
        @ServiceConnection
        fun rabbitContainer(): RabbitMQContainer = rabbitmq

        @Bean
        fun mailhogContainer(): GenericContainer<*> = mailhog

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            val container = postgresContainer()
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
            registry.add("spring.rabbitmq.host", rabbitmq::getHost)
            registry.add("spring.rabbitmq.port") { rabbitmq.amqpPort }

            // Only add MailHog host and port to override existing test properties
            registry.add("spring.mail.host", mailhog::getHost)
            registry.add("spring.mail.port") { mailhog.getMappedPort(1025) }
            registry.add("spring.mail.username") { "" }
            registry.add("spring.mail.password") { "" }
            registry.add("spring.mail.properties.mail.smtp.auth") { "false" }
            registry.add("spring.mail.properties.mail.smtp.starttls.enable") { "false" }

            registry.add("mailhog.web.port") { mailhog.getMappedPort(8025) }
        }

    }
}